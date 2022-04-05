object Credentials {
    private fun getEnv(key: String): String = System.getenv(key) ?: ""

    val appCenterTokenDevelopment: String = getEnv("APP_CENTER_API_TOKEN_DEVELOPMENT")
    val appCenterTokenProduction: String = getEnv("APP_CENTER_API_TOKEN_PRODUCTION")

    val storePassword: String = getEnv("KEYSTORE_STORE_PASSWORD")

    val keyAliasDebug: String = getEnv("KEYSTORE_DEBUG_ALIAS")
    val keyPasswordDebug: String = getEnv("KEYSTORE_DEBUG_PASSWORD")

    val keyAliasRelease: String = getEnv("KEYSTORE_RELEASE_ALIAS")
    val keyPasswordRelease: String = getEnv("KEYSTORE_RELEASE_PASSWORD")
}