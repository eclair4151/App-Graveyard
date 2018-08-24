package com.shayesapps.gifts.NetworkingServices;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import com.rosaloves.bitlyj.Bitly;
import com.rosaloves.bitlyj.Url;
import com.shayesapps.gifts.Fragments.WebViewFragment;
import com.shayesapps.gifts.Models.Gift;
import com.shayesapps.gifts.Models.SearchResult;
import com.shayesapps.gifts.R;
import static com.rosaloves.bitlyj.Bitly.*;
/**
 * Created by tomer on 8/9/14.
 */
public class RequestUtil {
    public static RequestQueue queue;
    public static ImageLoader imageLoader;
    public static Bitly.Provider bitly;
    private static Response.ErrorListener errorListener;
    private static Context context;
    //all static browse arrays
    public static String[] categories = {"Accessories","Alcohol & Tobacco","Arts & Crafts","Automobile","Baby","Beauty & Spa","Books & Magazines","Business & Executive","Care Packages","Charitable","Clothing","Collectibles & Memorabilia","Computers & Electronics","Eco-Friendly","Experiential","Flowers & Plants","Food & Beverages","For Travel","For the Garden & Yard","For the Home","For the Office","Gag Gifts","Gift Baskets","Gift Cards & Memberships","Golf","Health & Nutrition","Home Aid","Jewelry & Watches","Kid's Accessories","Kid's Clothing","Maternity","Music, Movies, & TV","Musical Instruments","Nostalgic/Retro","Party Favors","Patriotic","Personalized","Pet Accessories","Pet Toys & Treats","Pet Wearables","Photo Related","Political","Religious","Romantic","Room Décor","School & Dorm","Seasonal","Sporting Good & Apparel","Sports Fan Gear","Toys & Games","Unique"};
    public static String[] recipients = {"Baby Boy","Baby Girl","Boy","Girl","Teen Boy","Teen Girl","Male College Student","Female College Student","Adult Male","Adult Female","Male Senior Citizen","Female Senior Citizen","Pet"};
    public static String[] personalities = {"Achiever","Activist","All-Star Athlete","Creative Thinker","Do-it-Yourselfer","Fitness & Health Nut","Geek","Girly Girl","Grandparent","Guy's Guy","Hipster","Host/Hostess","Intellectual","Outdoor Adventurer","Professional","Proud Parent","Spiritual Soul","Sports Fan","Trendy","Wild Child","Wiz Kid","X-treme Dude/Dudette"};
    public static String[] occasions = {"Anniversary","April Fools","Assistants' Day","Baby Shower","Bachelor Party","Bachelorette Party","Back to School","Bar/Bat Mitzvah","Birthday","Boss's Day","Bridal Shower","Christening/Baptism","Christmas","Cinco De Mayo","Communion","Confirmation","Easter","Engagement","Father's Day","Fourth of July","Get Well Soon","Graduation","Grandparent's Day","Halloween","Hanukkah","Housewarming","Kwanzaa","Memorial Day","Mother's Day","New Baby","New Job","New Year's","Passover","Retirement","St. Patrick's Day","Sympathy","Thanksgiving","Thoughts & Sentiments","Valentine's Day","Wedding"};
    public static String[] categoriesCodes = {"70T","ga2","Beb","iid","8RH","JXy","YU1","Y1E","UHI","Jbh","c18","6iC","KIF","Ads","ab5","BzI","LHd","nsA","e93","MN4","QU6","qkA","Jcb","wUW","80u","Kbl","H4I","TiW","W8c","Ku9","qjB","shh","pMR","PCV","6kf","mSz","1vV","2A1","QvK","lD2","Csa","atN","mf1","fIk","I1i","hHH","W1w","KPd","F3r","RKq","Yw1"};
    public static String[] recipientsCodes = {"ECA","Jab","SAZ","uJr","w7z","BQW","opq","cAA","8iE","rzS","qxB","bDl","CQf"};
    public static String[] personalitiesCodes = {"0HJ","QjG","qhU","WcJ","q33","BkJ","W1p","Iyt","l3L","6nI","hAj","1MU","M3a","zke","1xH","OWu","VWJ","6jn","NZO","kx1","17i","SR7"};
    public static String[] occasionsCodes = {"ob6","ENC","ZQQ","nVa","O7Q","wYT","WGP","7GV","Nwx","MyX","Rxp","S0D","pe6","Ph4","pfY","UXn","3jw","GeY","G0T","3rs","zFk","1lu","o6D","aRy","P9G","st8","LOD","OdK","IMX","yL9","vVe","ZI7","hoF","VQ0","3QK","0Xz","LLW","5qq","nC0","rsY"};


    //setup cache, image loader, and bitly api
    public void init(final Context context)
    {
        this.context = context;
        queue = Volley.newRequestQueue(context);
        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        imageLoader = new ImageLoader(Volley.newRequestQueue(context), imageCache);
        bitly = Bitly.as(context.getResources().getString(R.string.bitly_username), context.getResources().getString(R.string.bitly_key));
    }

    //take a request with parameters add api key, create a url and make request, pass in a listener for when the request finished
    public static void getSearchResult(List<NameValuePair> parameters, final Response.Listener listener)
    {
        parameters.add(new BasicNameValuePair("api_key",RequestUtil.context.getResources().getString(R.string.api_key)));

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError.networkResponse!=null)
                {
                    Log.e("Volley Error", Arrays.toString(volleyError.networkResponse.data));
                }
                else
                {
                    Toast.makeText(context, "Please connect to the internet to use this app", Toast.LENGTH_LONG).show();
                }
                listener.onResponse(null);
            }
        };

        try
        {
            URI url = URIUtils.createURI("http",context.getResources().getString(R.string.api_domain),-1,"/v2/search/product.json", URLEncodedUtils.format(parameters,"UTF-8"),null);
            Log.i("Request sent",url.toASCIIString());
            RequestUtil.queue.add(new JacksonRequest<SearchResult>(Request.Method.GET,url.toASCIIString(), null, SearchResult.class, listener, errorListener));
        }
        catch (URISyntaxException e)
        {
            Log.e("VOLLEY URL CREATION ERROR",e.getMessage());
        }

    }

    public static Url shortenUrl(String url)
    {
        return bitly.call(shorten(url));
    }

    //load popup browser from anywhere in the app from this functions
    public static void loadPopupBrowser(android.app.FragmentManager fm,String url, Gift gift)
    {
        Bundle b = new Bundle();
        b.putString("url",url);
        WebViewFragment webView = new WebViewFragment();
        webView.gift = gift;
        webView.setArguments(b);
        webView.show(fm, null);
    }

    //remove random html codes that android doesnt support
    public static String removeHTMLCodes(String html)
    {
        html = html.replace("&amp;#151;","--");
        html = html.replace("&amp;copy;","©");
        html = html.replace("&quot;","\"");
        html = html.replace("&amp;quot;","\"");
        html = html.replace("&amp;","&");
        html = html.replace("&#174;","®");
        html = html.replace("&#0174;","®");
        html = html.replace("&#8211;","-");
        html = html.replace("&euro;","€");
        html = html.replace("&nbsp;"," ");
        return html;
    }

}
