import okhttp3.*
import java.net.InetAddress
import java.util.concurrent.TimeUnit

fun main() {
    val ip = "104.16.123.45"
    val domain = "cloudflaremirrors.com"
    
    val client = OkHttpClient.Builder()
        .connectTimeout(2, TimeUnit.SECONDS)
        .readTimeout(2, TimeUnit.SECONDS)
        .dns(object : Dns {
            override fun lookup(hostname: String): List<InetAddress> {
                if (hostname == domain) {
                    return listOf(InetAddress.getByName(ip))
                }
                return Dns.SYSTEM.lookup(hostname)
            }
        })
        .build()

    val request = Request.Builder()
        .url("https://$domain/cdn-cgi/trace")
        .build()

    try {
        client.newCall(request).execute().use { response ->
            println("Response code: ${response.code}")
            println(response.body?.string())
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
