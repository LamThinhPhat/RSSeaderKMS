package com.example.ssreaderkms

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ssreaderkms.Adapters.MyNewsAdapter
import com.example.ssreaderkms.Models.News
import com.example.ssreaderkms.Services.XMLDOMParser
import org.w3c.dom.Element
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection
import java.util.regex.Matcher
import java.util.regex.Pattern


class SSRReaderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_s_s_r_reader, container, false)

        val temp = ReadRSS().execute("https://vnexpress.net/rss/tin-moi-nhat.rss").get()

        val listNewsRV = view.findViewById<RecyclerView>(R.id.NewsRV)

        var newsAdapter =MyNewsAdapter(listNewsItem)

        listNewsRV.adapter = newsAdapter

        listNewsRV.layoutManager =LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    class ReadRSS : AsyncTask<String, Int, String>() {
        override fun doInBackground(vararg p0: String?): String {
            return ReadDataFromURL(p0[0].toString())!!
        }

        override fun onPostExecute(result: String?) {
            val parser = XMLDOMParser()
            val document = parser.getDocument(result)

            var nodeList = document!!.getElementsByTagName("item")
            var nodeListDescription = document!!.getElementsByTagName("description")
            var str: String = ""

            for (i in 0..nodeList.length-1)
            {
                var temp = News()
                val cdata :String = nodeListDescription.item(i).textContent

                val checkPaternImg = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>") //Patern for image
                val matcher :Matcher = checkPaternImg.matcher(cdata)

                if (matcher.find())
                {
                    temp.imageURL = matcher.group(1).toString()
                }
                val element = nodeList.item(i) as Element
                str+= parser.getValue(element, "title")
                temp.title = parser.getValue(element, "title").toString()
                temp.linkPage = parser.getValue(element,"link").toString()
                listNewsItem.add(temp)
            }
            super.onPostExecute(result)
        }
    }

    companion object {
        var listNewsItem = ArrayList<News>()
        fun newInstance(){

        }
        //read content for URL
        fun ReadDataFromURL(Url: String): String? {
            val content = StringBuilder()
            try {
                val url = URL(Url)

                val urlConnection: URLConnection = url.openConnection()

                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.getInputStream()))
                var line: String = bufferedReader.readLine()

                while (line != null) {

                    content.append(line + "\n")
                    line = bufferedReader.readLine()
                }

                bufferedReader.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return content.toString()
        }
    }
}