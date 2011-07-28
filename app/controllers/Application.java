package controllers;

import play.*;
import play.mvc.*;
import java.util.*;
import models.*;

public class Application extends Controller {

	public static void index() {
		Post frontPost = Post.find("order by postedAt desc").first();
		System.out.println(Post.count() + " posts were found");
		System.out.println(User.count() + " user were found");
		render(frontPost);
	}

}