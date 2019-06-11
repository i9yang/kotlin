package ssg.front.common.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object MonitorInformation {
    val LOG: Logger = LoggerFactory.getLogger(MonitorInformation::class.java)

    fun sendTelegramLogFile(message: String, group: String) {
        try {
            val chatId = "-319500078"
            val boundary = java.lang.Long.toHexString(System.currentTimeMillis())
            val charset = "UTF-8"
            val CRLF = "\r\n"

            val obj = URL("https://api.telegram.org/bot230732747:AAGaOJSfuesOOGa449UF2ijcm_kINBfagkc/sendDocument?chat_id=$chatId")

            val con = obj.openConnection() as HttpsURLConnection
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
            con.doOutput = true

            con.outputStream.use { output ->
                PrintWriter(OutputStreamWriter(output, charset), true).use { writer ->
                    writer.append("--$boundary").append(CRLF)
                    writer.append("Content-Disposition: form-data; name=\"document\"; filename=\"$group.log\"").append(CRLF)
                    writer.append("Content-Type: text/plain; charset=$charset").append(CRLF)
                    writer.append(CRLF).flush()
                    writer.append(message).flush()
                    writer.append(CRLF).flush()
                    writer.append("--$boundary--").append(CRLF).flush()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
