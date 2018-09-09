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

### **Step1:**











