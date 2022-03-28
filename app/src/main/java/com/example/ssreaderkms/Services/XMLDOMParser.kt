package com.example.ssreaderkms.Services

import android.util.Log
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.io.IOException
import java.io.StringReader
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException


//Convert content read to Document
class XMLDOMParser {

    fun getDocument(xml: String?): Document? {
        var document: Document? = null
        val factory = DocumentBuilderFactory.newInstance()
        document = try {
            val db: DocumentBuilder = factory.newDocumentBuilder()
            val inputSource = InputSource()
            inputSource.setCharacterStream(StringReader(xml))
            inputSource.setEncoding("UTF-8")

            db.parse(inputSource)
        } catch (e: ParserConfigurationException) {
            Log.e("Error: ",  e.message.toString())
            return null
        } catch (e: SAXException) {
            Log.e("Error: ", e.message.toString())
            return null
        } catch (e: IOException) {
            Log.e("Error: ", e.message.toString())
            return null
        }
        return document
    }

    fun getValue(item: Element, name: String?): String? {
        val nodes: NodeList = item.getElementsByTagName(name)
        return getTextNodeValue(nodes.item(0))
    }

    private fun getTextNodeValue(elem: Node?): String {
        var child: Node
        if (elem != null) {
            if (elem.hasChildNodes()) {
                child = elem.getFirstChild()
                while (child != null) {
                    if (child.getNodeType() === Node.TEXT_NODE) {
                        return child.getNodeValue()
                    }
                    child = child.getNextSibling()
                }
            }
        }
        return ""
    }

}