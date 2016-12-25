#Medical-Conference-Reminder

An Android application that provides managing and tracking of medical conference of doctors.

##Functional Specification

An Android application that doctors can use to participate in and track medical conference schedules.

For conference admins
 - can schedule new conferences
 - can send conference calendar invites to doctors
 - can see suggested new/old topics
For doctors
 - can suggest new topics they could speak on
 - can see suggested new/old topics
 - can receive / accept and reject invites
 - invites are added to calendar



1. How to build: 
To build proj, please use Android studio with gradle. You will need API 25 SDK installed on your machine. 

2. Initialize DB - not required 
DB will initialise itself with demo data. 

5. Feedback 
I a not very goog designer - it would be great, if design screens would be given with assignment, in PSD format, for ex. 
Also, that was a little bit time-consuming project - I spend 17 hours to write the code, but I was familiar with all, except GreenDao ORM. 


Features, implemented in application: 

Crop photo functionality for profile picture 
Popular ORM - GreenDao library for database (Before I used Active Android ORM, but it is deprecated now) 
UniversalImageLoader for photo loading and caching 
ButterKnife bindings 
