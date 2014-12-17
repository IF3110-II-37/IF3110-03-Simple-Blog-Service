/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.cxf.helpers.IOUtils;

/**
 *
 * @author Ahmad Zaky
 */
public class SimpleBlogServiceImplementation implements SimpleBlogService {
    /* Konstanta terkait Firebase */
    private final String FIREBASE_URL = "https://intense-heat-3835.firebaseio.com/";
    private final String FIREBASE_POST = "post";
    private final String FIREBASE_USER = "user";
    private final String FIREBASE_COMMENT = "comment";
    private final String FIREBASE_ID_COUNTER = "id_counter";
    private final Firebase firebaseRoot = new Firebase(FIREBASE_URL);
    private final Firebase firebasePost = firebaseRoot.child(FIREBASE_POST);
    private final Firebase firebaseUser = firebaseRoot.child(FIREBASE_USER);
    private final Firebase firebaseComment = firebaseRoot.child(FIREBASE_COMMENT);

    /* Kelas yang menangani hasil transaksi dengan Firebase */
    public class TransactionResult implements Firebase.CompletionListener {
        
        private AtomicBoolean _done = new AtomicBoolean(false);
        private boolean _success = true;
        
        @Override
        public void onComplete(FirebaseError error, Firebase frbs) {
            _done.set(true);
            if (error != null) {
                _success = false;
            }
            else {
                _success = true;
            }
        }
        
        public boolean done() {
            return _done.get();
        }
        
        public boolean success() {
            return _success;
        }
    }
    
    /* Mengembalikan post id baru */
    private Integer getNewPostId() {
        Integer id = 0;
        try {
            // retrieve value from firebase
            URL url = new URL(FIREBASE_URL + FIREBASE_POST + "/" + FIREBASE_ID_COUNTER + ".json");
            URLConnection connection = url.openConnection();
            id = Integer.parseInt(IOUtils.toString(connection.getInputStream()));
            
            // update id and update the value in firebase
            id++;
            firebasePost.child(FIREBASE_ID_COUNTER).setValue(id);
        }
        catch (IOException e) {
            // do nothing
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public boolean addPost(String judul, String konten, String tanggal, Integer status) {
        // masukkan konten ke HashMap
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", getNewPostId().toString());
        map.put("title", judul);
        map.put("content", konten);
        map.put("date", tanggal);
        map.put("published", status.toString());
        
        // push ke firebase
        TransactionResult result = new TransactionResult();
        firebasePost.push().setValue(map, result);
        
        // tunggu hingga transaksi nya selesai
        while (!result.done());
        return result.success();
    }

    @Override
    public model.Post getPostById(Integer id) {
        return new model.Post();
    }

    @Override
    public List<model.Post> listPost() {
        List<model.Post> list = new ArrayList<model.Post>();
        model.Post post = new model.Post();
        post.setTitle("Ahmad Ganteng");
        post.setContent("Eldwin si Coy");
        list.add(post);
        return list;
    }

    @Override
    public boolean updatePost(Integer id, String judul, String tanggal, String konten, Integer status) {
        return true;
    }

    @Override
    public boolean deletePost(Integer id) {
        return true;
    }

    @Override
    public boolean restorePost(Integer id) {
        return true;
    }

    @Override
    public boolean permanentDeletePost(Integer id) {
        return true;
    }

    @Override
    public boolean publishPost(Integer id) {
        return true;
    }

    @Override
    public boolean unpublishPost(Integer id) {
        return true;
    }

    @Override
    public boolean addUser(String username, String password, String email, String role) {
        return true;
    }

    @Override
    public List<model.User> listUser() {
        List<model.User> list = new ArrayList<model.User>();
        return list;
    }

    @Override
    public model.User getUserById(Integer id) {
        return new model.User();
    }

    @Override
    public model.User getUserByUsername(String username) {
        return new model.User();
    }

    @Override
    public boolean modifyUser(Integer id, String username, String password, String email, String role) {
        return true;
    }

    @Override
    public boolean removeUser(Integer id) {
        return true;
    }

    @Override
    public boolean addComment(Integer postId, String nama, String email, String konten) {
        return true;
    }

    @Override
    public List<model.Comment> listComment(Integer postId) {
        List<model.Comment> list = new ArrayList<model.Comment>();
        return list;
    }

    @Override
    public List<model.Post> search(String query) {
        List<model.Post> list = new ArrayList<model.Post>();
        return list;
    }
    
}