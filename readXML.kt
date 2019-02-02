private fun downloadXml(urlPath: String?): String {
    val xmlResult = StringBuilder()

    try {
//                    Connect to the internet and download the data
        val url = URL(urlPath)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        val response = connection.responseCode
        Log.d(TAG, "downloadXml: Response Code: $response")

//      These three lines can be reduced to a single line as shown on line 17
//      val inputStream = connection.inputStream
//      val inputStreamReader = InputStreamReader(inputStream)
//      val reader = BufferedReader(inputStreamReader)

//      https://developer.android.com/reference/java/io/BufferedReader
        val reader = BufferedReader(InputStreamReader(connection.inputStream))

        val inputBuffer = CharArray(500) // Arbitrary number and can be altered
        var charsRead = 0
//      If the BufferedReader returns a value less than zero, there is no more data.
        while (charsRead >= 0) {
            charsRead = reader.read(inputBuffer)
            
//           It is possible for the charsRead to be zero, that doesn't mean there
//           won't be more data to follow, thus we still continue to loop. See Note 1
            if (charsRead > 0) {
                xmlResult.append(String(inputBuffer, 0, charsRead))
            }
            
/*
*           Note 1:
*           With the Java classes we are using here, the test for charsRead > 0 is redundant
*           as if there is not data available and we haven't reached the end of the stream
*           then the reader will wait until there is more data available.
*           Note all readers work this way, so this block of code has been written defensively
*           to allow for the reader to be changed at a later date.
* */
        }
        
//       Closing the bufferedReader will close the InputStreamReader and the connection
        reader.close()
        Log.d(TAG, "downloadXml: Received: ${xmlResult.length} bytes ")
        return xmlResult.toString()

    } catch (e: MalformedURLException) {
        Log.e(TAG, "downloadXml: Invalid URL ${e.message}")
    } catch (e: IOException) {
        Log.e(TAG, "downloadXml: IO Exception ${e.message}")
    } catch (e: Exception) {
        Log.e(TAG, "downloadXml: Unknown error ${e.message}")
    }

// If it get to here there was an error. Return empty string
    return "" 
}
