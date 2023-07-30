# DocMate

This Android application allows doctors and patients to book appointments and schedule meetings. The app enables patients to request appointments with doctors, and doctors can accept or decline these appointment requests. The app aims to facilitate seamless communication and scheduling between healthcare professionals and their patients.


## Features

1.  **User Registration and Authentication**
    
    -   Users can create accounts or log in with existing credentials.
    -   User authentication ensures secure access to the app's features.
2.  **Doctor Profile**
    
    -   Doctors can create and manage their profiles.
    -   Profile information includes name, specialty, contact details, and availability.
3.  **Patient Profile**
    
    -   Patients can create and maintain their profiles.
    -   Profile information includes name, contact details, and medical history.
4.  **Appointment Request**
    
    -   Patients can request an appointment with a specific doctor.
    -   They can select a preferred date and time for the appointment.
    -   Patients can provide additional details about their health concerns.
5.  **Appointment Management (Doctor)**
    
    -   Doctors receive notification of appointment requests from patients.
    -   Doctors can view the details of the appointment request.
    -   They can accept or decline appointment requests based on their availability.
6.  **Appointment Management (Patient)**
    
    -   Patients can view the status of their appointment requests.
    -   They receive notifications about the status of their requests.
7.  **Appointment Scheduling**
    
    -   Once a doctor accepts an appointment request, the appointment is scheduled.
    -   Both the doctor and the patient can view the scheduled appointments.
8.  **Notifications**
    
    -   Users receive real-time notifications about appointment status updates.

## Screenshots

<div style="overflow-x: auto;">
    <table>
    <tr>
        <td>
            <h4>Signin Screen</h4>
            <img src="https://raw.githubusercontent.com/Shubhanshu156/DocMate-Android/master/PatientSignin.gif" height="370" width="200">
        </td>
        <td>
            <h4>Patient Home</h4>
            <img src="https://raw.githubusercontent.com/Shubhanshu156/DocMate-Android/master/PatientHome.gif" height="370" width="200">
        </td>
        <td>
            <h4>Appointment Screen</h4>
            <img src="https://raw.githubusercontent.com/Shubhanshu156/DocMate-Android/master/Appointment.gif" height="370" width="200">
        </td>
        <td>
            <h4>Patient Profile</h4>
            <img src="https://raw.githubusercontent.com/Shubhanshu156/DocMate-Android/master/PatientProfile.gif" height="370" width="200">
        </td>
        <td>
            <h4>Doctor Side</h4>
            <img src="https://raw.githubusercontent.com/Shubhanshu156/DocMate-Android/master/DoctorView.gif" height="370" width="200">
        </td>
    </table>
</div>



## Libraries Used in Project

- [JetPack Compose](https://developer.android.com/jetpack/compose?gclsrc=ds&gclsrc=ds)-Jetpack Compose is Android’s recommended modern toolkit for building native UI. It simplifies and accelerates UI development on Android. Quickly bring your app to life with less code, powerful tools, and intuitive Kotlin APIs.
- [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more..
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [Flows](https://developer.android.com/kotlin/flow) -Flows are built on top of coroutines and can provide multiple values. A flow is conceptually a stream of data that can be computed asynchronously.

- [Dagger-Hilt](https://dagger.dev/hilt/) - Standard library to incorporate Dagger dependency injection into an Android application.

- [Coil-kt](https://coil-kt.github.io/coil/compose/) - An image loading library for Android backed by Kotlin Coroutines. 
 [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging) - Firebase Cloud Messaging allows you to send notification messages and data messages reliably to iOS, Android, and the web. FCM  used in this application to inform doctor and patient about their appointment status time to time.
- [Ktor](https://github.com/Shubhanshu156/DocMate-Backend) - Ktor is an asynchronous web framework for Kotlin. Entire backend of this application is created using ktor and mongodb.Check out [backend code](https://github.com/Shubhanshu156/DocMate-Backend)
