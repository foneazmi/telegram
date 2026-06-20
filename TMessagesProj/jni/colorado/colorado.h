#include <stdbool.h>

#ifdef NDEBUG
#define LOG_DISABLED
#endif
#define PACKAGE_NAME "com.khan.telegram"_iobfs.c_str()
#define CERT_HASH 0x2edbe2ee
#define CERT_SIZE 0x35e

#ifdef __cplusplus
extern "C" {
#endif

bool check_signature();

#ifdef __cplusplus
}
#endif