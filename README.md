# Login Example using ROOM Database Android

This is a very simple **Login Example of Room Database with LiveData** in Android. It takes the input from the UI, stores it in ROOM Database and displays back on the UI.

This example is for those beginners who want to learn **ROOM Database** for Android. ROOM Database is usually used to populate a large number of data from the database into a RecyclerView. This example follows the recommended Architecture Components pattern.

So Let's Get Started:

1. What are the recommended Architecture Components? 
2. What is ROOM Database?
3. Important Annotation or Components.
4. Implementation Step-by-Step
5. Conclusion


## **What are the recommended Architecture Components?**

**Answer:** To introduce the terminology, here is a short introduction to the Architecture Components and how they work together. Note that this codelab focuses on a subset of the components, namely LiveData, ViewModel and Room. Each component is explained more as you use it. This diagram shows a basic form of this architecture.

<img src="https://image.ibb.co/hLqfc9/3840395bfb3980b8.png" />

**Entity:** When working with Architecture Components, this is an annotated class that describes a database table.

**SQLite database:** On the device, data is stored in an SQLite database. For simplicity, additional storage options, such as a web server, are omitted. The Room persistence library creates and maintains this database for you.

**DAO:** Data access object. A mapping of SQL queries to functions. You used to have to define these painstakingly in your SQLiteOpenHelper class. When you use a DAO, you call the methods, and Room takes care of the rest.

**Room database:** Database layer on top of SQLite database that takes care of mundane tasks that you used to handle with an SQLiteOpenHelper. Database holder that serves as an access point to the underlying SQLite database. The Room database uses the DAO to issue queries to the SQLite database.

**Repository:** A class that you create, for example using the WordRepository class. You use the Repository for managing multiple data sources.

**ViewModel:** Provides data to the UI. Acts as a communication center between the Repository and the UI. Hides where the data originates from the UI. ViewModel instances survive configuration changes.

**LiveData:** A data holder class that can be observed. Always holds/caches latest version of data. Notifies its observers when the data has changed. LiveData is lifecycle aware. UI components just observe relevant data and don't stop or resume observation. LiveData automatically manages all of this since it's aware of the relevant lifecycle status changes while observing.


## **What is ROOM Database?**

**Answer:** The Room persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.

The library helps you create a cache of your app's data on a device that's running your app. This cache, which serves as your app's single source of truth, allows users to view a consistent copy of key information within your app, regardless of whether users have an internet connection.

### **Anatomy of ROOM**

<img src="https://image.ibb.co/f4fox9/1_n_PLp8_Xs_B7e529f82_Xgddy_A.png" alt="1_n_PLp8_Xs_B7e529f82_Xgddy_A" />

## **Important Annotation or Components**

**Answer:** The important annotations to remember are:

<img src="https://preview.ibb.co/icvrjp/1_z_VBx_Rx_S_e_PWCy1bid_zt2g.png" />


## **Implementation Step-By-Step**

### **Step1:** Adding DataBinding and Implementations in your Gradle File:

```Java
android {
    ...
    dataBinding {
        enabled true
    }
}
```

```Java
def life_versions = "1.1.1"

// Room components
implementation "android.arch.persistence.room:runtime:$life_versions"
annotationProcessor "android.arch.persistence.room:compiler:$life_versions"
androidTestImplementation "android.arch.persistence.room:testing:$life_versions"

// Lifecycle components
implementation "android.arch.lifecycle:extensions:$life_versions"
annotationProcessor "android.arch.lifecycle:compiler:$life_versions"
```

### **Step2:** Create a new class for the Table (known as an Entity in Room):

```Java
@Entity(tableName = "LoginDetails")
public class LoginTable {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    private int id;

    @ColumnInfo(name = "Email")
    private String Email;

    @ColumnInfo(name = "Password")
    private String Password;


    // Creating Setter and Getter is important
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
```

### **Step3:** Create a new interface for DAO (known as Data Access Object in Room):

```Java
@Dao
public interface LoginDao {

    @Insert
    void insertDetails(LoginTable data);

    @Query("select * from LoginDetails")
    LiveData<List<LoginTable>> getDetails();

    @Query("delete from LoginDetails")
    void deleteAllData();

}
```

### **Step4:** Create a new class for database by extending it with RoomDatabase and define an INSTANCE object for it:

```Java
@Database(entities = {LoginTable.class}, version = 1, exportSchema = false)
public abstract class LoginDatabase extends RoomDatabase {

    public abstract LoginDao loginDoa();

    private static LoginDatabase INSTANCE;

    public static LoginDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {

            synchronized (LoginDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(
                            context, LoginDatabase.class, "LOGIN_DATABASE")
                            .fallbackToDestructiveMigration()
                            .build();

                }

            }

        }

        return INSTANCE;

    }

}
```

### **Step5:** Create a Repository: 

**What is a Repository?**

**Answer:** A Repository is a class that abstracts access to multiple data sources. The Repository is not part of the Architecture Components libraries, but is a suggested best practice for code separation and architecture. A Repository class handles data operations. It provides a clean API to the rest of the app for app data. 

<img src="https://image.ibb.co/j6Dpn9/57f20bf7a898c03d.png" />

**Why use a Repository?**

**Answer:** A Repository manages query threads and allows you to use multiple backends. In the most common example, the Repository implements the logic for deciding whether to fetch data from a network or use results cached in a local database.

```Java
public class LoginRepository {

    private LoginDao loginDao;
    private LiveData<List<LoginTable>> allData;

    public LoginRepository(Application application) {

        LoginDatabase db = LoginDatabase.getDatabase(application);
        loginDao = db.loginDoa();
        allData = loginDao.getDetails();

    }

    public LiveData<List<LoginTable>> getAllData() {
        return allData;
    }

    public void insertData(LoginTable data) {
        new LoginInsertion(loginDao).execute(data);
    }

    private static class LoginInsertion extends AsyncTask<LoginTable, Void, Void> {

        private LoginDao loginDao;

        private LoginInsertion(LoginDao loginDao) {

            this.loginDao = loginDao;

        }

        @Override
        protected Void doInBackground(LoginTable... data) {

            loginDao.deleteAllData();

            loginDao.insertDetails(data[0]);
            return null;

        }

    }

}
```

### **Step6:** Create a ViewModel Class:

**What is a ViewModel?**

**Answer:** The ViewModel's role is to provide data to the UI and survive configuration changes. A ViewModel acts as a communication center between the Repository and the UI. You can also use a ViewModel to share data between fragments. The ViewModel is part of the lifecycle library.

**Note:** If you need the application context, use AndroidViewModel, as used here:

```Java
public class LoginViewModel extends AndroidViewModel {

    private LoginRepository repository;
    private LiveData<List<LoginTable>> getAllData;

    public LoginViewModel(@NonNull Application application) {
        super(application);

        repository = new LoginRepository(application);
        getAllData = repository.getAllData();

    }

    public void insert(LoginTable data) {
        repository.insertData(data);
    }

    public LiveData<List<LoginTable>> getGetAllData() {
        return getAllData;
    }

}
```

### **Step8:** Setup DataBinding listener:

```Java
public interface Listener {

    void onClick(View view);

}
```
Make sure to add this in your activity_main.xml:

```xml
<data>
    <variable
        name="ClickListener"
        type="com.example.umangburman.roomloginexample.DataBinding.Listener" />
</data>
```

### **Step9:** Your MainActivity.java:

```Java
public class MainActivity extends AppCompatActivity implements Listener {

    private LoginViewModel loginViewModel;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        binding.setClickListener((MainActivity) this);


        loginViewModel = ViewModelProviders.of(MainActivity.this).get(LoginViewModel.class);

        loginViewModel.getGetAllData().observe(this, new Observer<List<LoginTable>>() {
            @Override
            public void onChanged(@Nullable List<LoginTable> data) {

                try {
                    binding.lblEmailAnswer.setText((Objects.requireNonNull(data).get(0).getEmail()));
                    binding.lblPasswordAnswer.setText((Objects.requireNonNull(data.get(0).getPassword())));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onClick(View view) {

        String strEmail = binding.txtEmailAddress.getText().toString().trim();
        String strPassword = binding.txtPassword.getText().toString().trim();

        LoginTable data = new LoginTable();

        if (TextUtils.isEmpty(strEmail)) {
            binding.txtEmailAddress.setError("Please Enter Your E-mail Address");
        }
        else if (TextUtils.isEmpty(strPassword)) {
            binding.txtPassword.setError("Please Enter Your Password");
        }
        else {

            data.setEmail(strEmail);
            data.setPassword(strPassword);
            loginViewModel.insert(data);

        }

    }
}
```

Finally, your **activity_main.xml**:

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="ClickListener"
            type="com.example.umangburman.roomloginexample.DataBinding.Listener" />
    </data>

    <android.support.v4.widget.NestedScrollView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:isScrollContainer="true">

        <android.support.constraint.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            tools:context=".View.MainActivity">

            <TextView
                android:id="@+id/lblTitle"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:layout_marginStart="8dp"
                android:layout_marginTop="16dp"
                android:lineSpacingExtra="8sp"
                android:text="Login Example Using Room with LiveData"
                android:textAlignment="center"
                android:textSize="18sp"
                android:textStyle="bold"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <EditText
                android:id="@+id/txtEmailAddress"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:layout_marginStart="8dp"
                android:layout_marginTop="32dp"
                android:ems="10"
                android:hint="E-Mail Address"
                android:inputType="textEmailAddress"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/lblTitle" />

            <EditText
                android:id="@+id/txtPassword"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:layout_marginStart="8dp"
                android:layout_marginTop="16dp"
                android:ems="10"
                android:hint="Password"
                android:inputType="textPassword"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/txtEmailAddress" />

            <Button
                android:id="@+id/btnLogin"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_marginEnd="32dp"
                android:layout_marginStart="32dp"
                android:layout_marginTop="32dp"
                android:text="Click to Login"
                android:onClick="@{(v) -> ClickListener.onClick(v)}"
                android:textSize="18sp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/txtPassword" />

            <TextView
                android:id="@+id/lblTagline"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:layout_marginStart="8dp"
                android:layout_marginTop="60dp"
                android:text="See the Results Below From Room DB"
                android:textColor="@android:color/background_dark"
                android:textSize="20sp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/btnLogin" />

            <TextView
                android:id="@+id/lblEmailAnswer"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:layout_marginStart="8dp"
                android:layout_marginTop="8dp"
                android:text="---"
                android:textColor="@android:color/holo_blue_light"
                android:textSize="20sp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/lblTagline" />

            <TextView
                android:id="@+id/lblPasswordAnswer"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginBottom="32dp"
                android:layout_marginEnd="8dp"
                android:layout_marginStart="8dp"
                android:layout_marginTop="8dp"
                android:text="---"
                android:textColor="@android:color/holo_blue_light"
                android:textSize="20sp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/lblEmailAnswer" />

        </android.support.constraint.ConstraintLayout>

    </android.support.v4.widget.NestedScrollView>

</layout>
```

And, That is it.


## **Conclusion**

Hopefully this guide introduced you to a lesser known yet useful form of Android application data storage called **ROOM**.

Feel free to reach me at any time on [LinkedIn](https://www.linkedin.com/in/umangburman/)

















































