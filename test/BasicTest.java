import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

  @Before
  public void setup() {
    Fixtures.deleteAll();
  }

  @Test
  public void createAndRetrieveUser() {


    //Create a new user and save it

    User user = new User("bob@gmail.com", "secret_password", "Bob");
    user.save();


    //Find an user with email address runeroniek@gmail.com

    User bob = User.find("byEmail", "bob@gmail.com").first();


    //Test it

    assertNotNull(bob);
    assertEquals("Bob", bob.fullname);
  }

  @Test
  public void tryConnectAsUser() {


    //Create a new User and Save it
    User user = new User("bob@gmail.com", "secret", "Bob");
    user.save();


    //Check if data is valid (user and password)
    assertNotNull(User.connect("bob@gmail.com", "secret"));
    assertNull(User.connect("bob@gmail.com", "bad_password"));
    assertNull(User.connect("bob@gmail.cam", "secret"));

  }

  @Test
  public void createPost() {


    //Create a new user
    User user = new User("bob@gmail.com", "secret", "Bob");
    user.save();

    //Create a new post

    Post post = new Post(user, "My first Post", "Hello world");
    post.save();

    // Test that post was created

    assertEquals(1, Post.count());

    //All posts by bob

    List<Post> bobPosts = Post.find("byAuthor", user).fetch(); //Here this test will fail :-)


    //Tests

    assertEquals(1, bobPosts.size());
    Post firstPost = bobPosts.get(0);

    assertNotNull(firstPost);
    assertEquals(user, firstPost.author);
    assertEquals("My first Post", firstPost.title);
    assertEquals("Hello world", firstPost.content);
    assertNotNull(firstPost.postedAt);



  }

  @Test
  public void postComment() {

    //Create user
    User user = new User("bob@gmail.com", "secret", "Bob");
    user.save();

    //Create a post with the user previously created
    Post bob = new Post(user, "First post with comment", "Post content");
    bob.save();

    //Create 2 comments with non-existent characters
    Comment firstComment = new Comment(bob, "Moron", "I really enjoy this website rs").save();
    Comment secondComment = new Comment(bob, "Douche", "What is this I don't even").save();

    //Retrieve all comments

    List<Comment> bobPostComments = Comment.find("byPost", bob).fetch();

    //How many posts?

    assertEquals(2, bobPostComments.size());
    Comment first = bobPostComments.get(0);
    Comment second = bobPostComments.get(1);

    // Null posts?

    assertNotNull(first);
    assertNotNull(second);

    //Check if all comments content are the following rs
    assertEquals("Moron", first.author);
    assertEquals("Douche", second.author);

  }

  @Test
  public void useTheCommentsRelation() {


    // new user
    User bob = new User("bob@gmail.com", "secret", "Bob").save();

    // new post

    Post post = new Post(bob, "Title", "Content").save();


    // create the comments...
    post.addComment("Jeff", "Boaa");
    post.addComment("Jim", "Minhoca");

    // ... and count the comments
    assertEquals(1, User.count());
    assertEquals(1, Post.count());
    assertEquals(2, Comment.count());

    // Retrieve bob post

    Post bobPost = Post.find("byAuthor", bob).first();
    assertNotNull(bobPost);

    // checking the comments from BOB
    assertEquals(2, bobPost.comments.size());
    assertEquals("Jeff", bobPost.comments.get(0).author);

    // Delete the post...

    bobPost.delete();

    // ...and see if there are comments from that post

    assertEquals(1, User.count());
    assertEquals(0, Post.count());
    assertEquals(0, Comment.count());

  }


}
