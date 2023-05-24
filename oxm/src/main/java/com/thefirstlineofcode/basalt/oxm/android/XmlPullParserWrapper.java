package com.thefirstlineofcode.basalt.oxm.android;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * @author xb.zou
 * @date 2017/10/26
 * @option XmlPullParser封装类
 * 解决Android端使用时getColumn()方法获取数值与在java端获取时不一致问题
 */
public class XmlPullParserWrapper implements XmlPullParser {
    private XmlPullParser xmlPullParser;
    private int startColumn;

    public XmlPullParserWrapper(XmlPullParser xmlPullParser) {
        super();
        this.xmlPullParser = xmlPullParser;
        // 设置初始的列序号
        startColumn = xmlPullParser.getColumnNumber();
    }


    @Override
    public void setFeature(String s, boolean b) throws XmlPullParserException {
        xmlPullParser.setFeature(s, b);
    }

    @Override
    public boolean getFeature(String s) {
        return xmlPullParser.getFeature(s);
    }

    @Override
    public void setProperty(String s, Object o) throws XmlPullParserException {
        xmlPullParser.setProperty(s, o);
    }

    @Override
    public Object getProperty(String s) {
        return xmlPullParser.getProperty(s);
    }

    @Override
    public void setInput(Reader reader) throws XmlPullParserException {
        xmlPullParser.setInput(reader);
    }

    @Override
    public void setInput(InputStream inputStream, String s) throws XmlPullParserException {
        xmlPullParser.setInput(inputStream, s);
    }

    @Override
    public String getInputEncoding() {
        return xmlPullParser.getInputEncoding();
    }

    @Override
    public void defineEntityReplacementText(String s, String s1) throws XmlPullParserException {
        xmlPullParser.defineEntityReplacementText(s, s1);
    }

    @Override
    public int getNamespaceCount(int i) throws XmlPullParserException {
        return xmlPullParser.getNamespaceCount(i);
    }

    @Override
    public String getNamespacePrefix(int i) throws XmlPullParserException {
        return xmlPullParser.getNamespacePrefix(i);
    }

    @Override
    public String getNamespaceUri(int i) throws XmlPullParserException {
        return xmlPullParser.getNamespaceUri(i);
    }

    @Override
    public String getNamespace(String s) {
        return xmlPullParser.getNamespace(s);
    }

    @Override
    public int getDepth() {
        return xmlPullParser.getDepth();
    }

    @Override
    public String getPositionDescription() {
        return xmlPullParser.getPositionDescription();
    }

    @Override
    public int getLineNumber() {
        return xmlPullParser.getLineNumber();
    }

    @Override
    public int getColumnNumber() {
        return xmlPullParser.getColumnNumber() - startColumn;
    }

    @Override
    public boolean isWhitespace() throws XmlPullParserException {
        return xmlPullParser.isWhitespace();
    }

    @Override
    public String getText() {
        return xmlPullParser.getText();
    }

    @Override
    public char[] getTextCharacters(int[] ints) {
        return xmlPullParser.getTextCharacters(ints);
    }

    @Override
    public String getNamespace() {
        return xmlPullParser.getNamespace();
    }

    @Override
    public String getName() {
        return xmlPullParser.getName();
    }

    @Override
    public String getPrefix() {
        return xmlPullParser.getPrefix();
    }

    @Override
    public boolean isEmptyElementTag() throws XmlPullParserException {
        return xmlPullParser.isEmptyElementTag();
    }

    @Override
    public int getAttributeCount() {
        return xmlPullParser.getAttributeCount();
    }

    @Override
    public String getAttributeNamespace(int i) {
        return xmlPullParser.getAttributeNamespace(i);
    }

    @Override
    public String getAttributeName(int i) {
        return xmlPullParser.getAttributeName(i);
    }

    @Override
    public String getAttributePrefix(int i) {
        return xmlPullParser.getAttributePrefix(i);
    }

    @Override
    public String getAttributeType(int i) {
        return xmlPullParser.getAttributeType(i);
    }

    @Override
    public boolean isAttributeDefault(int i) {
        return xmlPullParser.isAttributeDefault(i);
    }

    @Override
    public String getAttributeValue(int i) {
        return xmlPullParser.getAttributeValue(i);
    }

    @Override
    public String getAttributeValue(String s, String s1) {
        return xmlPullParser.getAttributeValue(s, s1);
    }

    @Override
    public int getEventType() throws XmlPullParserException {
        return xmlPullParser.getEventType();
    }

    @Override
    public int next() throws XmlPullParserException, IOException {
        return xmlPullParser.next();
    }

    @Override
    public int nextToken() throws XmlPullParserException, IOException {
        return xmlPullParser.nextToken();
    }

    @Override
    public void require(int i, String s, String s1) throws XmlPullParserException, IOException {
        xmlPullParser.require(i, s, s1);
    }

    @Override
    public String nextText() throws XmlPullParserException, IOException {
        return xmlPullParser.nextText();
    }

    @Override
    public int nextTag() throws XmlPullParserException, IOException {
        return xmlPullParser.nextTag();
    }
}
