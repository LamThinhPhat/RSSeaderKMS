package com.example.ssreaderkms

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ssreaderkms.Adapters.MyNewsAdapter
import com.example.ssreaderkms.Models.News
import com.example.ssreaderkms.Services.XMLDOMParser
import com.example.ssreaderkms.SplashActivity.Companion.accountUserLogin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dmax.dialog.SpotsDialog
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
        currentContext = this.context
        listNewsRV = view.findViewById<RecyclerView>(R.id.NewsRV)

        newsAdapter = MyNewsAdapter(listNewsItem, currentContext!!)

        listNewsRV!!.adapter = newsAdapter
        listNewsRV!!.layoutManager =LinearLayoutManager(currentContext,LinearLayoutManager.VERTICAL,false)

        listNewsRV!!.addItemDecoration(
            DividerItemDecoration(currentContext,
            DividerItemDecoration.VERTICAL)
        )

        newsAdapter.onMarkClick = { newsItem, markIsCHecked -> //Click the star
            if (markIsCHecked)
            {
                accountUserLogin!!.markList.add(newsItem)
                updateMarkListInDB()
            }
            else
            {
                accountUserLogin!!.markList.remove(newsItem)
                updateMarkListInDB()
            }
        }

        newsAdapter.onClickNews = {linkPage ->
            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra("UrlPage", linkPage)
            startActivity(intent)

        }

        alertDialog = SpotsDialog.Builder().setContext(activity)
            .setCancelable(false)
            .setMessage("Uploading")
            .build()

        urlEdt = view.findViewById(R.id.urlEditText)
        searchBtn = view.findViewById(R.id.SearchUrlBtn)

        return view
    }

    private fun updateMarkListInDB() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val accountRef = FirebaseDatabase.getInstance().getReference("Account/${uid}")
        accountRef.setValue(accountUserLogin)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchBtn!!.setOnClickListener {
            listNewsItem.clear()
            var tempUrl = urlEdt!!.text.toString()
            if (!tempUrl.contains("https://"))
                tempUrl = "https://${tempUrl}"
            alertDialog.show()
            ReadRSS().execute(tempUrl)
        }
    }

    companion object {
        var listNewsItem = ArrayList<News>()
        var listNewsRV : RecyclerView? = null
        var currentContext: Context? = null
        lateinit var newsAdapter : MyNewsAdapter
        var urlEdt : EditText? = null
        var searchBtn : Button? = null

        lateinit var alertDialog :android.app.AlertDialog

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

    //Read RSS object
    class ReadRSS : AsyncTask<String, Int, String>() {
        override fun doInBackground(vararg p0: String?): String {
            return ReadDataFromURL(p0[0].toString())!!
        }

        override fun onPostExecute(result: String?) {
            try {
                val parser = XMLDOMParser()
                val document = parser.getDocument(result)

                var nodeList = document!!.getElementsByTagName("item")
                var nodeListDescription = document!!.getElementsByTagName("description")
                var str: String = ""

                for (i in 0..nodeList.length-1)
                {
                    var temp = News()
                    val cdata :String = nodeListDescription.item(i+1).textContent

                    val checkPaternImg = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>") //Patern for image
                    val matcher :Matcher = checkPaternImg.matcher(cdata)

                    if (matcher.find())
                    {
                        temp.imageURL = matcher.group(1).toString()
                        val element = nodeList.item(i) as Element
                        str+= parser.getValue(element, "title")
                        temp.title = parser.getValue(element, "title").toString()
                        temp.linkPage = parser.getValue(element,"link").toString()
                        listNewsItem.add(temp)
                    }
                }
                newsAdapter.notifyDataSetChanged()
                alertDialog.dismiss()
            }
            catch (e:Throwable)
            {
                alertDialog.dismiss()
                Toast.makeText(currentContext, "Invalid URL", Toast.LENGTH_LONG).show()
                Log.e("Error", e.message.toString())
            }

            super.onPostExecute(result)
        }
    }
}