package nl.wolfpackit.wolfpack.database;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;
import java.util.concurrent.Callable;

@android.arch.persistence.room.Database(entities = {Declaration.class, Account.class, DeclarationFeedback.class, Receipt.class, TravelDeclaration.class, News.class}, version = 16, exportSchema = false)
public abstract class Database extends RoomDatabase{
    //creating a single database instance
    static Database db;
    public static Database getDatabase(){
        return db;
    }
    public static Database createDatabase(Context context){
        db = Room.databaseBuilder(context, Database.class, "database-name").fallbackToDestructiveMigration().build();
        return db;
    }

    //setting up the database interaction
    public abstract DeclarationDao declarationDao();
    public abstract Account.AccountDao accountDao();
    public abstract DeclarationFeedbackDao declarationFeedbackDao();
    public abstract TravelDeclarationDao travelDeclarationDao();
    public abstract ReceiptDao receiptDao();
    public abstract NewsDao newsDao();

    //helper methods for the interaction
    public interface QueryResult<T>{
        void onResult(T o);
    }
    @SuppressLint("StaticFieldLeak")
    private void createTask(final Callable c, final QueryResult callback){
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {
                try {
                    return c.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object result){
                if(callback!=null)
                    callback.onResult(result);
            }
        }.execute();
    }

    //creating simple database interface methods
    //account methods
    public static void getAccount(final GoogleSignInAccount googleAccount, QueryResult<Account> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                return db.accountDao().get(googleAccount.getId());
            }
        }, res);
    }
    public static void getAccount(final String ID, QueryResult<Account> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                return db.accountDao().get(ID);
            }
        }, res);
    }
    public static void createAccount(GoogleSignInAccount googleAccount, QueryResult<Account> res){
        final Account account = new Account();
        account.attachGoogleAccount(googleAccount);
        account.setName(googleAccount.getDisplayName());
        db.createTask(new Callable(){
            public Object call() throws Exception {
                db.accountDao().insert(account);
                return account;
            }
        }, res);
    }
    public static void updateAccount(final Account account, QueryResult<Account> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                db.accountDao().update(account);
                return account;
            }
        }, res);
    }

    //declaration methods
    public static void getDeclaration(final String ID, QueryResult<Declaration> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                return db.declarationDao().get(ID);
            }
        }, res);
    }
    public static void getDeclarationByName(final String name, QueryResult<Declaration> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                return db.declarationDao().findByName(name);
            }
        }, res);
    }
    public static void getDeclarationsAccepted(QueryResult<List<Declaration>> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                return db.declarationDao().getAllApproved();
            }
        }, res);
    }
    public static void getDeclarationsDenied(QueryResult<List<Declaration>> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                return db.declarationDao().getAllDenied();
            }
        }, res);
    }
    public static void getDeclarationsPending(QueryResult<List<Declaration>> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                return db.declarationDao().getAllPending();
            }
        }, res);
    }
    public static void createDeclaration(QueryResult<Declaration> res){
        final Declaration declaration = new Declaration();
        db.createTask(new Callable(){
            public Object call() throws Exception {
                db.declarationDao().insertAll(declaration);
                return declaration;
            }
        }, res);
    }
    public static void updateDeclaration(final Declaration declaration, QueryResult<Declaration> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                db.declarationDao().update(declaration);
                return declaration;
            }
        }, res);
    }


    //declaration feedback methods
    public static void getDeclarationFeedback(final String ID, QueryResult<DeclarationFeedback> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                return db.declarationFeedbackDao().get(ID);
            }
        }, res);
    }
    public static void getDeclarationFeedback(final Declaration declaration, QueryResult<DeclarationFeedback> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                return db.declarationFeedbackDao().findByDeclarationID(declaration.getID());
            }
        }, res);
    }
    public static void createDeclarationFeedback(QueryResult<DeclarationFeedback> res){
        final DeclarationFeedback declarationFeedback = new DeclarationFeedback();
        db.createTask(new Callable(){
            public Object call() throws Exception {
                db.declarationFeedbackDao().insertAll(declarationFeedback);
                return declarationFeedback;
            }
        }, res);
    }
    public static void updateDeclarationFeedback(final DeclarationFeedback declarationFeedback, QueryResult<DeclarationFeedback> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                db.declarationFeedbackDao().update(declarationFeedback);
                return declarationFeedback;
            }
        }, res);
    }


    //travel declaration methods
    public static void getTravelDeclaration(final String ID, QueryResult<TravelDeclaration> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                return db.travelDeclarationDao().get(ID);
            }
        }, res);
    }
    public static void getTravelDeclaration(final Declaration declaration, QueryResult<TravelDeclaration> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                return db.travelDeclarationDao().findByDeclarationID(declaration.getID());
            }
        }, res);
    }
    public static void createTravelDeclaration(QueryResult<TravelDeclaration> res){
        final TravelDeclaration travelDeclaration = new TravelDeclaration();
        db.createTask(new Callable(){
            public Object call() throws Exception {
                db.travelDeclarationDao().insertAll(travelDeclaration);
                return travelDeclaration;
            }
        }, res);
    }
    public static void updateTravelDeclaration(final TravelDeclaration travelDeclaration, QueryResult<TravelDeclaration> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                db.travelDeclarationDao().update(travelDeclaration);
                return travelDeclaration;
            }
        }, res);
    }

    //receipt methods
    public static void getReceipt(final String ID, QueryResult<Receipt> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                return db.receiptDao().get(ID);
            }
        }, res);
    }
    public static void getReceipt(final Declaration declaration, QueryResult<Receipt> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                return db.receiptDao().findByDeclarationID(declaration.getID());
            }
        }, res);
    }
    public static void createReceipt(QueryResult<Receipt> res){
        final Receipt receipt = new Receipt();
        db.createTask(new Callable(){
            public Object call() throws Exception {
                db.receiptDao().insertAll(receipt);
                return receipt;
            }
        }, res);
    }
    public static void updateReceipt(final Receipt receipt, QueryResult<Receipt> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                db.receiptDao().update(receipt);
                return receipt;
            }
        }, res);
    }

    //news_article methods
    public static void getNews(QueryResult<List<News>> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                return db.newsDao().getAll();
            }
        }, res);
    }
    public static void getNews(final String ID, QueryResult<News> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                return db.newsDao().get(ID);
            }
        }, res);
    }
    public static void createNews(QueryResult<News> res){
        final News news = new News();
        db.createTask(new Callable(){
            public Object call() throws Exception {
                db.newsDao().insertAll(news);
                return news;
            }
        }, res);
    }
    public static void updateNews(final News news, QueryResult<News> res){
        db.createTask(new Callable(){
            public Object call() throws Exception {
                db.newsDao().update(news);
                return news;
            }
        }, res);
    }
}
