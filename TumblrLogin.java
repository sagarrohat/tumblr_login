import java.util.Map;
import java.util.Scanner;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TumblrLogin {

	private String login_url = "https://www.tumblr.com/login";
	private String bblog_url = "https://www.tumblr.com/services/bblog";
	private String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36";
	private String email;
	private String password;
	private Scanner input;
	
	public TumblrLogin() {
		input = new Scanner(System.in);
		System.out.print("Email : ");
		email = input.nextLine();
		System.out.println("Password : ");
		password = input.nextLine();
	}
	
	public void login() throws Exception{
		
		Response login_page = Jsoup.connect(login_url).method(Method.GET).execute();
		Document login_document = login_page.parse();
		
		String random_username_suggestions = Jsoup.parse(login_document.select("input[name=random_username_suggestions]").attr("value")).text();
		String form_key = login_document.select("input[name=form_key]").attr("value");
				
		Map<String, String> login_page_cookies = login_page.cookies();
		

		Jsoup
			.connect(bblog_url)
			.cookies(login_page_cookies)
			.referrer(login_url)
			.ignoreHttpErrors(true).followRedirects(true)
			.userAgent(user_agent)
			.method(Method.POST)
	        .execute();
		
		Response login_response = Jsoup.connect(login_url)
				.data("determine_email", email)
				.data("user[email]",email)
				.data("user[password]",password)
				.data("tumblelog[name]","")
				.data("user[age]","")
				.data("context","other")
				.data("version","STANDARD")
				.data("follow","")
				.data("http_referer","https://www.tumblr.com/logout")
				.data("form_key", form_key)
				.data("seen_suggestion","0")
				.data("used_suggestion","0")
				.data("used_auto_suggestion","0")
				.data("about_tumblr_slide","")
				.data("random_username_suggestions", random_username_suggestions)
				.cookies(login_page_cookies)
				.userAgent(user_agent)
				.method(Method.POST)
	            .execute();
		
		Document login_reponse_document = login_response.parse();
		
		String user_id = login_reponse_document.select("input[name=t]").attr("value");
		
		System.out.println(user_id);
		
	}
	
	public static void main(String[] args) throws Exception {
		new TumblrLogin().login();
	}
	
}