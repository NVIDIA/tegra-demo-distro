DESCRIPTION = "Tegra reference image with the Holoscan SDK. Its SDK \
(-c populate_sdk) and extensible SDK (-c populate_sdk_ext) cross-build Holoscan \
applications against the image's sysroot."

require demo-image-reference.inc

HOLOSCAN_TEST_PACKAGES = " \
    holoscan-sdk-benchmarks \
    holoscan-sdk-examples \
    ${@bb.utils.contains('DISTRO_FEATURES', 'ptest', 'holoscan-sdk-ptest', '', d)} \
    holoscan-camera-examples \
    ${@bb.utils.contains('DISTRO_FEATURES', 'ptest', 'holoscan-camera-ptest', '', d)} \
"

CORE_IMAGE_BASE_INSTALL += "holoscan-sdk python3-holoscan-sdk holoscan-camera python3-jetson-io ${HOLOSCAN_TEST_PACKAGES}"

# The test payload is for the board, not for cross-building applications; in
# the SDK it would drag in PyTorch, CuPy and pytest along with their -dev packages.
TOOLCHAIN_TARGET_TASK:remove = "${HOLOSCAN_TEST_PACKAGES} python3-jetson-io"

# holoscan-sdk-dev reaches the SDK sysroot through the dev-pkgs complementary
# packages. The eSDK ignores this and takes holoscan-sdk's own -native tools
# from the image's build dependencies instead.
TOOLCHAIN_HOST_TASK += "nativesdk-packagegroup-holoscan-sdk-host"

# CUDA, TensorRT and Holoscan make the default dbg-pkgs and src-pkgs run to
# several GB. Headers, libraries and CMake packages are enough to build against.
SDKIMAGE_FEATURES = "dev-pkgs"

# Holoscan's exported interface links flatbuffers::flatbuffers, which
# flatbuffers exports as its static library.
TOOLCHAIN_TARGET_TASK += "flatbuffers-staticdev"
