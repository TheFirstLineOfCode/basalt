package com.thefirstlineofcode.basalt.oxm.android;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author xb.zou
 * @date 2020/2/19
 * @option
 */
public interface IXmlParserFactory {
    XmlPullParser createParserWrapper(String message) throws XmlPullParserException;
}
