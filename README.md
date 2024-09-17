# SocialMediaAnalyticsHub
A multi-user social media platform with analytics functionality, built using Java and JavaFX. Users can sign up, log in, post updates, and perform analyses on social media content to gain insights such as the most liked or shared posts.

## Project Details
IDE: IntelliJ

Java version: 20

JavaFX version: 20

Database: SQLite

Github repository: https://github.com/seemaotomorrow/AP-A2-DataAnalyticsHub

## How to run program from command line:

1. Clone the repository:`git clone https://github.com/seemaotomorrow/AP-A2-DataAnalyticsHub`

2. Navigate to the project directory:`cd AP-A2-DataAnalyticsHub`

3. Run the program using Gradle:`./gradlew run`

## Functionalities:

1. User Management:
- Users can sign up and log in to the system.
- Users can also choose to sign up as a VIP user for additional features.

2. Post Creation:
- Users can manually add posts to their collection using the “Add a Post” feature.
- Post IDs are automatically incremented, so users do not need to specify an ID.
- The logged-in user is automatically assigned as the author of their posts.

3. Bulk Import (VIP Users Only):

- VIP users can import multiple posts from a CSV file. The system automatically assigns the logged-in user as the author for each post.

4. Post Retrieval & Deletion:

- Users can retrieve or delete posts by post ID, but only for posts in their own collection.

5. Post Analytics:

- Users can retrieve the top N posts based on likes. They can choose to retrieve posts from either their own collection or the entire database.



## Reference:
1. JavaFX Login and Signup Form with Database Connection, https://www.youtube.com/watch?v=ltX5AtW9v30
2. How to save files using a File Chooser in JavaFX? https://www.tutorialspoint.com/how-to-save-files-using-a-file-chooser-in-javafx
