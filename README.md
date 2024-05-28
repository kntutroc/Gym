# Gym App

This is a fitness application designed to help users plan and track their workouts. It provides a variety of features including exercise recommendations, workout logging, and progress tracking.

## Features

- **Exercise Search:** Users can search for exercises based on muscle groups and difficulty levels.
- **Workout Logging:** Log your workouts with exercise names.
- **Saved Routines:** View and manage your saved workout routines.
- **Personalized Recommendations:** Receive exercise recommendations based on your fitness goals and preferences.
- **Profile Management:** Manage your profile information.

## Technologies Used

- **Firebase:** For user authentication and data storage.
- **Glide:** Image loading library for displaying exercise images.
- **Android Architecture Components:** Utilized for building robust, maintainable, and testable Android apps.
- **API Integration:** Integrated with external APIs to fetch exercise data.
- **Kotlin:** Programming language used for Android app development.
- **Gson:** For converting Java objects to JSON and vice versa.
- **OkHttp:** HTTP client for making network requests.
- **JUnit:** For unit testing.
- **Mockito:** For mocking in tests.
- **Robolectric:** For running Android tests on the JVM.
- **Mockk:** For mocking in Kotlin tests.

## Getting Started

To get started with the project, follow these steps:

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Build and run the project on an Android device or emulator.

## Testing

- **Emulator used for testing:** Pixel 3 API 34
- **Physical device used for testing:** Xiaomi MI 9T API 29
- **Compile SDK:** 34
- **Min SDK:** 29

### Unit and Instrumentation Tests

- Unit tests and instrumentation tests are located in `test`.
- **Unit Tests:** Use JUnit, Mockito, Robolectric, and Mockk.
- **Instrumentation Tests:** Use Espresso and Mockito.

## Additional Features

- In the top-right corner menu (three dots), you can update your data, add a profile photo, view privacy policies, and log out.
- In your profile, you can view your profile picture, your personal data, calculate BMI, and track your weight and measurements.
- In "My Routines," you can search for routines, add new routines, and delete existing ones.
- In "Recommended Routines," you can find routines based on your level: beginner, intermediate, or advanced. Each routine includes a name, a gif demonstrating the exercise, the muscles being used, and a description of the benefits (e.g., cardiovascular system improvement).
- There is a button on the main pages to report any issues with the app.
- Logging in is easy with email and password, and there's an option to reset your password.

## APIs Used

- **Firebase**
- **ExerciseDb from Rapid API**
- **Exercises API from API Ninja**
