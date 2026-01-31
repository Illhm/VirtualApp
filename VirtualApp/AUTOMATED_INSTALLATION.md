# Automated Installation & Configuration

This modified version of VirtualApp supports automated installation of Google Mobile Services (GMS), Google Play Store, and Tricky Store (KeyBox Spoofer) for each cloned profile.

## Setup Instructions

To enable automated installation, you need to provide the necessary APK files and configuration files on the host device's storage.

### 1. File Structure

Create the following directory structure on your device's internal storage (typically `/sdcard/`):

```
/sdcard/VirtualApp/
├── plugins/
│   ├── com.google.android.gms.apk          (Google Play Services)
│   ├── com.android.vending.apk             (Google Play Store)
│   ├── com.google.android.gsf.apk          (Google Services Framework)
│   ├── io.github.vvb2060.keyattestation.apk (Tricky Store / KeyBox Spoofer)
│   └── ... (Other GMS components if needed)
└── keybox.xml                              (KeyBox Configuration File)
```

**Note:** The APK filenames in `plugins/` must match the package name + `.apk`. For example, if the package name is `com.android.vending`, the file must be named `com.android.vending.apk`.

### 2. Supported Packages

The system automatically looks for and installs the following packages if found in `plugins/` or installed on the host system:

- **Google Play Services**: `com.google.android.gms`
- **Google Play Store**: `com.android.vending`
- **Google Services Framework**: `com.google.android.gsf`
- **Tricky Store**: `io.github.vvb2060.keyattestation` (or whichever package name is defined in `GmsSupport.TRICKY_STORE_PKG`)

### 3. KeyBox Configuration

To configure KeyBox for each profile:
1.  Place your valid `keybox.xml` file at `/sdcard/VirtualApp/keybox.xml`.
2.  When a new profile (user) is created, this file is automatically copied to the private storage of the Tricky Store app within that profile.

## How it Works

1.  **Startup**: When VirtualApp starts, it checks if GMS and Tricky Store are installed for the main user (User 0). If not, it attempts to install them from `/sdcard/VirtualApp/plugins/`. If the file is not found there, it tries to clone the app from the host system.
2.  **New Profile**: When you clone an app that triggers the creation of a new user space (e.g., "Space 2"), the system automatically:
    -   Installs GMS and Play Store into the new space.
    -   Installs Tricky Store into the new space.
    -   Copies `keybox.xml` to the new space's secure storage.

This ensures every clone has a fully functional environment with Google Services and KeyBox attestation capabilities.
