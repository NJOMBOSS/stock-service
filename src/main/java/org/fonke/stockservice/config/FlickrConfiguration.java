package org.fonke.stockservice.config;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@Configuration
public class FlickrConfiguration {

    @Value("${flickr.apikey}")
    private  String apipkey;

    @Value("${flickr.apiSecret}")
    private String apiSecret;

    @Value("${flickr.appkey}")
    private  String appkey;

    @Value("${flickr.appSecret}")
    private String appSecret;


/*
    @Bean
    public Flickr getFlickr() throws InterruptedException, ExecutionException, IOException, FlickrException {
        Flickr flickr = new Flickr(apipkey, apiSecret, new REST()); // création d'une instance

        // Création d'un objet de type authentification
        OAuth10aService service = new ServiceBuilder(apipkey)
                .apiSecret(apiSecret)
                .build(FlickrApi.instance(FlickrApi.FlickrPerm.DELETE)); // delete prend en compte la lecture, écriture et suppression

        final Scanner scanner = new Scanner(System.in);

        // récupération du requestToken
        final OAuth1RequestToken request = service.getRequestToken();

        // récupération de l'URL qui va permettre à l'applicationd d'utiliser l'api
        final String authUrl = service.getAuthorizationUrl(request);

        System.out.println(authUrl);
        System.out.println("Paste it here >>");

        final String authVerifier = scanner.nextLine();

        // on génére le accessToken pour l'application
        OAuth1AccessToken accessToken = service.getAccessToken(request, authVerifier);

        System.out.println(accessToken.getToken());
        System.out.println(accessToken.getTokenSecret());

        // Vérification si le Token est valide
        Auth auth = flickr.getAuthInterface().checkToken(accessToken);

        System.out.println("---------------------------------");
        System.out.println(auth.getToken());
        System.out.println(auth.getTokenSecret());

        return flickr;

    }*/
    @Bean
    public Flickr getFlickr(){

       Flickr flickr = new Flickr(apipkey, apiSecret, new REST());

        Auth auth = new Auth();

        auth.setPermission(Permission.DELETE);

        auth.setToken(appkey);
        auth.setTokenSecret(appSecret);

        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);

        flickr.setAuth(auth);

        return flickr;
    }
}
