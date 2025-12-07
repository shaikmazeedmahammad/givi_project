import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@SuppressWarnings("unchecked")
public class FeedbackSurveySystem {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> feedbackCollection;
    private MongoCollection<Document> surveyCollection;
    private Scanner scanner;
    
    // MongoDB connection details
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "feedback_system";
    private static final String FEEDBACK_COLLECTION = "feedbacks";
    private static final String SURVEY_COLLECTION = "surveys";
    
    public FeedbackSurveySystem() {
        try {
            // Initialize MongoDB connection
            mongoClient = MongoClients.create(CONNECTION_STRING);
            database = mongoClient.getDatabase(DATABASE_NAME);
            feedbackCollection = database.getCollection(FEEDBACK_COLLECTION);
            surveyCollection = database.getCollection(SURVEY_COLLECTION);
            scanner = new Scanner(System.in);
            
            System.out.println("Connected to MongoDB successfully!");
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
            System.err.println("Make sure MongoDB is running on localhost:27017");
        }
    }
    
    // FEEDBACK CRUD OPERATIONS
    
    // Create Feedback
    public void createFeedback() {
        System.out.println("\n=== CREATE NEW FEEDBACK ===");
        System.out.print("Enter user name: ");
        String userName = scanner.nextLine();
        
        System.out.print("Enter user email: ");
        String userEmail = scanner.nextLine();
        
        System.out.print("Enter feedback message: ");
        String message = scanner.nextLine();
        
        System.out.print("Enter rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        Document feedback = new Document("_id", new ObjectId())
                .append("userName", userName)
                .append("userEmail", userEmail)
                .append("message", message)
                .append("rating", rating)
                .append("createdAt", new java.util.Date());
        
        feedbackCollection.insertOne(feedback);
        System.out.println("Feedback created successfully! ID: " + feedback.getObjectId("_id"));
    }
    
    // Read All Feedbacks
    public void readAllFeedbacks() {
        System.out.println("\n=== ALL FEEDBACKS ===");
        List<Document> feedbacks = feedbackCollection.find().into(new ArrayList<>());
        
        if (feedbacks.isEmpty()) {
            System.out.println("No feedbacks found.");
            return;
        }
        
        for (Document feedback : feedbacks) {
            System.out.println("ID: " + feedback.getObjectId("_id"));
            System.out.println("Name: " + feedback.getString("userName"));
            System.out.println("Email: " + feedback.getString("userEmail"));
            System.out.println("Rating: " + feedback.getInteger("rating"));
            System.out.println("Message: " + feedback.getString("message"));
            System.out.println("Date: " + feedback.getDate("createdAt"));
            System.out.println("------------------------");
        }
    }
    
    // Update Feedback
    public void updateFeedback() {
        System.out.println("\n=== UPDATE FEEDBACK ===");
        System.out.print("Enter feedback ID to update: ");
        String feedbackId = scanner.nextLine();
        
        try {
            Document existingFeedback = feedbackCollection.find(eq("_id", new ObjectId(feedbackId))).first();
            
            if (existingFeedback == null) {
                System.out.println("Feedback not found!");
                return;
            }
            
            System.out.print("Enter new feedback message: ");
            String newMessage = scanner.nextLine();
            
            System.out.print("Enter new rating (1-5): ");
            int newRating = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            UpdateResult result = feedbackCollection.updateOne(
                eq("_id", new ObjectId(feedbackId)),
                combine(
                    set("message", newMessage),
                    set("rating", newRating),
                    set("updatedAt", new java.util.Date())
                )
            );
            
            if (result.getModifiedCount() > 0) {
                System.out.println("Feedback updated successfully!");
            } else {
                System.out.println("Failed to update feedback.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid feedback ID format!");
        } catch (Exception e) {
            System.out.println("Error updating feedback: " + e.getMessage());
        }
    }
    
    // Delete Feedback
    public void deleteFeedback() {
        System.out.println("\n=== DELETE FEEDBACK ===");
        System.out.print("Enter feedback ID to delete: ");
        String feedbackId = scanner.nextLine();
        
        try {
            DeleteResult result = feedbackCollection.deleteOne(eq("_id", new ObjectId(feedbackId)));
            
            if (result.getDeletedCount() > 0) {
                System.out.println("Feedback deleted successfully!");
            } else {
                System.out.println("Feedback not found!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid feedback ID format!");
        } catch (Exception e) {
            System.out.println("Error deleting feedback: " + e.getMessage());
        }
    }
    
    // SURVEY CRUD OPERATIONS
    
    // Create Survey
    public void createSurvey() {
        System.out.println("\n=== CREATE NEW SURVEY ===");
        System.out.print("Enter survey title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter survey description: ");
        String description = scanner.nextLine();
        
        System.out.print("Enter number of questions: ");
        int questionCount = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        List<Document> questions = new ArrayList<>();
        for (int i = 0; i < questionCount; i++) {
            System.out.print("Enter question " + (i + 1) + ": ");
            String question = scanner.nextLine();
            questions.add(new Document("question", question).append("questionNumber", i + 1));
        }
        
        Document survey = new Document("_id", new ObjectId())
                .append("title", title)
                .append("description", description)
                .append("questions", questions)
                .append("createdAt", new java.util.Date())
                .append("isActive", true);
        
        surveyCollection.insertOne(survey);
        System.out.println("Survey created successfully! ID: " + survey.getObjectId("_id"));
    }
    
    // Read All Surveys
    public void readAllSurveys() {
        System.out.println("\n=== ALL SURVEYS ===");
        List<Document> surveys = surveyCollection.find().into(new ArrayList<>());
        
        if (surveys.isEmpty()) {
            System.out.println("No surveys found.");
            return;
        }
        
        for (Document survey : surveys) {
            System.out.println("ID: " + survey.getObjectId("_id"));
            System.out.println("Title: " + survey.getString("title"));
            System.out.println("Description: " + survey.getString("description"));
            System.out.println("Active: " + survey.getBoolean("isActive"));
            System.out.println("Questions:");
            
            // Safe type casting with error handling
            Object questionsObj = survey.get("questions");
            if (questionsObj instanceof List) {
                List<Document> questions = (List<Document>) questionsObj;
                for (Document question : questions) {
                    System.out.println("  " + question.getInteger("questionNumber") + ". " + question.getString("question"));
                }
            }
            System.out.println("------------------------");
        }
    }
    
    // Update Survey Status
    public void updateSurveyStatus() {
        System.out.println("\n=== UPDATE SURVEY STATUS ===");
        System.out.print("Enter survey ID: ");
        String surveyId = scanner.nextLine();
        
        try {
            System.out.print("Set survey active? (true/false): ");
            boolean isActive = scanner.nextBoolean();
            scanner.nextLine(); // consume newline
            
            UpdateResult result = surveyCollection.updateOne(
                eq("_id", new ObjectId(surveyId)),
                set("isActive", isActive)
            );
            
            if (result.getModifiedCount() > 0) {
                System.out.println("Survey status updated successfully!");
            } else {
                System.out.println("Survey not found!");
            }
        } catch (Exception e) {
            System.out.println("Invalid input or survey ID! Please enter 'true' or 'false' for active status.");
        }
    }
    
    // Delete Survey
    public void deleteSurvey() {
        System.out.println("\n=== DELETE SURVEY ===");
        System.out.print("Enter survey ID to delete: ");
        String surveyId = scanner.nextLine();
        
        try {
            DeleteResult result = surveyCollection.deleteOne(eq("_id", new ObjectId(surveyId)));
            
            if (result.getDeletedCount() > 0) {
                System.out.println("Survey deleted successfully!");
            } else {
                System.out.println("Survey not found!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid survey ID format!");
        } catch (Exception e) {
            System.out.println("Error deleting survey: " + e.getMessage());
        }
    }
    
    // Display Menu
    public void displayMenu() {
        System.out.println("\n=== FEEDBACK & SURVEY MANAGEMENT SYSTEM ===");
        System.out.println("1. Create Feedback");
        System.out.println("2. View All Feedbacks");
        System.out.println("3. Update Feedback");
        System.out.println("4. Delete Feedback");
        System.out.println("5. Create Survey");
        System.out.println("6. View All Surveys");
        System.out.println("7. Update Survey Status");
        System.out.println("8. Delete Survey");
        System.out.println("9. Exit");
        System.out.print("Choose an option (1-9): ");
    }
    
    // Main application loop
    public void run() {
        while (true) {
            try {
                displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                
                switch (choice) {
                    case 1:
                        createFeedback();
                        break;
                    case 2:
                        readAllFeedbacks();
                        break;
                    case 3:
                        updateFeedback();
                        break;
                    case 4:
                        deleteFeedback();
                        break;
                    case 5:
                        createSurvey();
                        break;
                    case 6:
                        readAllSurveys();
                        break;
                    case 7:
                        updateSurveyStatus();
                        break;
                    case 8:
                        deleteSurvey();
                        break;
                    case 9:
                        System.out.println("Thank you for using Feedback & Survey System!");
                        closeConnection();
                        return;
                    default:
                        System.out.println("Invalid option! Please choose between 1-9.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number between 1-9.");
                scanner.nextLine(); // clear invalid input
            }
        }
    }
    
    // Close MongoDB connection
    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
        scanner.close();
    }
    
    public static void main(String[] args) {
        FeedbackSurveySystem system = new FeedbackSurveySystem();
        system.run();
    }
}