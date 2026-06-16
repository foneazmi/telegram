import struct
import zlib
import sys

APK_SIGNING_BLOCK_MAGIC = b"APK Sig Block 42"
APK_SIGNATURE_SCHEME_V2_BLOCK_ID = 0x7109871a
APK_SIGNATURE_SCHEME_V3_BLOCK_ID = 0xf05368c0
EOCD_MAGIC = 0x6054b50

def read_certificate(apk_path):
    with open(apk_path, 'rb') as f:
        size = f.seek(0, 2)
        print(f"File size: {size}")

        # Find EOCD by scanning from end
        for i in range(0x10000):
            comment_ofs = size - 2 - i
            if comment_ofs < 0:
                break
            f.seek(comment_ofs)
            comment_sz = struct.unpack('<H', f.read(2))[0]
            if comment_sz == i:
                eocd_ofs = comment_ofs - 20
                if eocd_ofs < 0:
                    continue
                f.seek(eocd_ofs)
                magic = struct.unpack('<I', f.read(4))[0]
                if magic == EOCD_MAGIC:
                    print(f"EOCD at offset: {eocd_ofs}")
                    break
        else:
            print("EOCD not found")
            return None

        f.seek(eocd_ofs + 16)
        central_dir_off = struct.unpack('<I', f.read(4))[0]
        print(f"Central directory at: {central_dir_off}")

        sig_block_abs_end = central_dir_off
        print(f"Signing block end: {sig_block_abs_end}")

        # Read block_sz_ and magic
        f.seek(sig_block_abs_end - 8 - 16)
        block_sz_ = struct.unpack('<Q', f.read(8))[0]
        magic = f.read(16)
        print(f"block_sz_: {block_sz_}")
        print(f"magic: {magic}")

        if magic != APK_SIGNING_BLOCK_MAGIC:
            print(f"Invalid signing block magic, trying pre-magic area")
            # Maybe no signing block - try looking at the end differently
            return None

        # Go to start of id-value pairs
        sig_block_start = sig_block_abs_end - 8 - block_sz_  # -8 for block_sz itself
        f.seek(sig_block_start + 8)  # skip block_sz at start
        print(f"Id-value pairs start at: {f.tell()}, end at {sig_block_start + 8 + block_sz_}")

        cert = None
        while True:
            pos = f.tell()
            remaining = sig_block_start + 8 + block_sz_ - pos
            if remaining < 8:
                break

            id_value_len = struct.unpack('<Q', f.read(8))[0]
            print(f"  id_value_len={id_value_len} at pos={pos}")
            if id_value_len == block_sz_ or remaining == id_value_len + 8:
                # This is the block_sz_ at end
                print("  -> end sentinel")
                break

            pair_start = f.tell()
            block_id = struct.unpack('<I', f.read(4))[0]
            print(f"  block_id=0x{block_id:08x} at pos={pair_start}")

            if block_id == APK_SIGNATURE_SCHEME_V2_BLOCK_ID or block_id == APK_SIGNATURE_SCHEME_V3_BLOCK_ID:
                if cert is not None:
                    print("Multiple signature blocks found")
                    return None

                # Parse v2/v3 signature block
                # Skip signer_sequence_len (4)
                seq_len = struct.unpack('<I', f.read(4))[0]
                print(f"  signer_sequence_len={seq_len}")
                # Skip 1st signer length (4)
                signer_len = struct.unpack('<I', f.read(4))[0]
                print(f"  signer_len={signer_len}")
                # Skip signed_data length (4)
                signed_data_len = struct.unpack('<I', f.read(4))[0]
                print(f"  signed_data_len={signed_data_len}")
                # Read digest_sequence_len and skip
                digest_seq_len = struct.unpack('<I', f.read(4))[0]
                print(f"  digest_seq_len={digest_seq_len}")
                f.read(digest_seq_len)
                # Read certificate_sequence_len
                cert_seq_len = struct.unpack('<I', f.read(4))[0]
                print(f"  cert_seq_len={cert_seq_len}")
                # Read 1st cert length + data
                cert_len = struct.unpack('<I', f.read(4))[0]
                print(f"  cert_len={cert_len}")
                cert = f.read(cert_len)
                print(f"  certificate read: {len(cert)} bytes")

                f.seek(pair_start + id_value_len - 4)
            else:
                f.seek(pair_start + id_value_len - 4)

        return cert

if __name__ == '__main__':
    apk = sys.argv[1]
    cert = read_certificate(apk)
    if cert:
        crc = zlib.crc32(cert) & 0xffffffff
        print(f"\nCERT_SIZE = 0x{len(cert):x}")
        print(f"CERT_HASH = 0x{crc:x}")
        print(f"// ({len(cert)} bytes, CRC32 = 0x{crc:08x})")
    else:
        print("Failed to extract certificate")
        sys.exit(1)
