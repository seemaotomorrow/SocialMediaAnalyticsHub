Name: Mingjian Mao
Student ID: s3968520
COSC1295 Assignment 2 S2 2023

IDE: IntelliJ
Java version: 20
JavaFX version: 20
Database: SQLite
Link to Github repository: https://github.com/seemaotomorrow/AP-A2-DataAnalyticsHub

* Summary
The application accept multiple user. Can analyse the social media post.

* How to run program from command line:

1. `git clone https://github.com/seemaotomorrow/AP-A2-DataAnalyticsHub`
2. `cd AP-A2-DataAnalyticsHub`
3. `./gradlew run`

* Assumptions
1. Assume if a new user sign up, they need to add posts to their collection manually by using the 'add a post function' in the system.
   Only the VIP user can bulk import posts from a CSV file

2. Auto incrementing post IDs.(when user adding a post, don't need to provide a post ID)

3. Assume the author of a post is the current logged-in user.(when user adding a post, don't need to provide Author, it will assign the 'username' to author)
   This assumption also apply when user import posts from a csv file

4. Assume when logged-in user retrieve/delete a post by post ID, they can only retrieve/delete a post in their own collection

5. Assume when logged-in user retrieve posts by top N likes, they have the options to retrieve from just their collection or
   retrieve from the whole database.




Reference:
1. JavaFX Login and Signup Form with Database Connection, https://www.youtube.com/watch?v=ltX5AtW9v30
2. How to save files using a File Chooser in JavaFX? https://www.tutorialspoint.com/how-to-save-files-using-a-file-chooser-in-javafx
