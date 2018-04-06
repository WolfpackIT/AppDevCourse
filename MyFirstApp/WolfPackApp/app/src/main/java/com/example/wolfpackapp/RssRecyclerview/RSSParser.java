package com.example.wolfpackapp.RssRecyclerview;

/**
 * Created by Wolfpack on 4/6/2018.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;



import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class RSSParser extends AsyncTask<Void,Void,Boolean> {

    Context c;
    InputStream is;
    RecyclerView rv;

    ProgressDialog pd;
    ArrayList<String> headlines=new ArrayList<>();

    public RSSParser(Context c, InputStream is, RecyclerView rv) {
        this.c = c;
        this.is = is;
        this.rv = rv;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Parse Data");
        pd.setMessage("Parsing.....Please wait");
        pd.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return this.parseRSS();
    }

    @Override
    protected void onPostExecute(Boolean isParsed) {
        super.onPostExecute(isParsed);

        pd.dismiss();
        if(isParsed)
        {
            //BIND
            rv.setAdapter(new MyAdapter(c,headlines));

        }else {
            Toast.makeText(c,"Unable To Parse",Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean parseRSS()
    {
        try
        {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser parser=factory.newPullParser();

            parser.setInput(is, null);
            String headline=null;
            headlines.clear();

            int event=parser.getEventType();
            Boolean isWebsiteTitle=true;

            do {
                String name=parser.getName();

                switch (event)
                {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        headline=parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(isWebsiteTitle)
                        {
                            isWebsiteTitle=false;
                        }else if(name.equals("title"))
                        {
                            headlines.add(headline);
                        }
                        break;

                }


                event=parser.next();

            }while (event != XmlPullParser.END_DOCUMENT);

            return true;


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
