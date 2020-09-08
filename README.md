# Traveler
Social Networking App for Travelers And Tourists 

# Description 
Traveler is an interactive social media platform for Android mobile application. It will be used by travelers and tourists or people who like to discover new places. The app will help them travel easily, cheaply, differently, and discover new places in new styles. Also, people, students will be able to make money through our platform. 

# What’s new
The platform is merging all the travelers’ needs in one platform which will make it easier and more efficient for travelers to find all their needs in one application.
In detail, users will be able to share different types of posts that were designed to meet their needs. Also, users will have completely new services that never excited like booking a local guide, creating group trips, and getting information about all cities around the world, booking flights, and hotels. And most importantly, our app is creating new job opportunities for many people as a local guide.

# Software Used 
* We App was developed in Andriod Studio, we used Java programming language to handle the backend and XML for the frontend.

* We used Firebase Authentication to handle the registration and logging in. Firebase Real-time Database to handle the data of the users, Firebase Storage to save the images. 

* We implement Weather API from “OpenWeatherMap'' to get the current weather around the world. 

* We used Google services to get the places near to certain locations (Like hospitals near to me). 

* We integrated a prayer time calculator from github to learn the prayers time in different places around the world. 


# Database 
 
In our project we used Firebase Database to manage and save the information of the users, we searched a lot to find something simpler, clear, easy to use and modern at the same time so we ended up using Firebase Real-time database. 
The official definition is that “The Firebase Real-time Database is a cloud-hosted database. Data is stored as JSON and synchronized in real time to every connected client. When you build cross-platform apps with our iOS, Android, and JavaScript SDKs, all of your clients share one Real-time Database instance and automatically receive updates with the newest data.” 
 
Our Database is composed from 12 different node to store different information we will describe them one by one:  
*	Users node: This node holds the personal and business information like name, Bio, Country, Profile image etc.… 
*	Posts node: To save all published “Normal Posts” with their details like publisher, time and date, Category and Comments. 
*	Likes node: To save likes on “Normal Posts” with the details. 
*	WhoGoing node: To save all who is going posts that have been published with post details. 
*	IsGoing node: To save all Going people between two cities who have interacted with 
“WhoGoing Posts” with the details. 
*	Schedules node: To save all Schedules posts that have been published with post details. 
*	QuestionsLikes node: To save likes on “Questions Posts” with the details. 8- Followers node: To save all followers for each user. 
*	Following node: To save all followings for each user. 
*	LocalGuides node: To save all local Guides registered in our app. 
*	Rooms node: To save all created rooms in our app. 
*	GuidesBook node: To save all Booking requests have been made. 
 
Users node is one of the most important nodes in the database, inside this node we are saving the number of posts liked for each post’s category, requests sent and received, joined rooms, all with their details. 
 
We have designed our posts to be published with a certain category, the aim of this is to know what categories does the user like the most. So, by saving and tracking the user's likes we will know him better and we will be able to recommend new things in our app after building a recommendation engine. 
All the collected information will be after the acceptance of the user. 

# Main Features

* Posting different types of posts.

    *	Discretional posts (description and images etc...)
    *	Questions.
    *	Cargo Posts (or “who is going” to send personal stuff)
    *	Scheduling Trips Posts.
    
* Working as a local guide.

* Boking a local guide.

* Creating Room (Group Trip).

* Information about Cities.

    *	General Information. 
    *	Tourist Locations. 
    *	Daily Schedules. 
    *	Hotels. 
    *	Weather. 
    *	Prayer Time. 
    
* Booking Flights and Hotels.


# Target Audience (who can use the app)

* All people who are interested in traveling, reading, and learning from other people’s experiences. 

* Travelers who like to share their experience and share undiscovered places. 

* People who will travel soon and look for schedules, activities, or any information about their destination.

* People looking for a local guide to show them the city.

* People and students who want to work as a local guide.

* People who are looking for a trip-partner who share similar interests. 



# Future Work

*	Building a recommendation engine based on the users’ usage and interactions on the app.

*	Creating “tourist locations finder” from photos using image processing. 

*	Making agreements with airlines companies, hotels and restaurants to give the user good offers.


# Links 

* [App on Google play](https://play.google.com/store/apps/details?id=com.app.demotraveler)

* [Full Document](https://drive.google.com/file/d/1O3FU4e78-M1ecpJhOX8FbgcDQbeDhIef/view?usp=sharing)

* [Real images from the app](https://drive.google.com/drive/folders/1A16CJDQVQp0inlYjXxgMhF9Sar7D2MNv?usp=sharing)


