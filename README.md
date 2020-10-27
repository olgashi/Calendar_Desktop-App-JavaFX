# Appointment Scheduling Application

## Overview

Scheduling desktop user interface application aimed to support an international consulting organization. The application uses MySQL database to store all data. 

### Notable features

- MySQL Database 

- Incorporation of lambda expressions

- Streams and filters to manipulate data more efficiently

- Java date/time API to support end-users in various geographical regions

- Advanced Exception Control

- Activity Log

### Entity Relationship Diagram

![Entity Relationship Diagram](https://github.com/olgashi/Appointment_Scheduling_Application/blob/master/Database_ERD_Update.png)

### Main View


### Functionality overview:

- log-in form that can determine the user’s location and translate log-in and error control messages into two languages.
- ability to add, update, and delete customer records in the database, including name, address, and phone number.
- ability to add, update, and delete appointments, capturing the type of appointment and a link to the specific customer record in the database.
- ability to view the calendar by month and by week.
- ability to automatically adjust appointment times based on user time zones and daylight saving time.
- exception controls to prevent each of the following: scheduling an appointment outside business hours, scheduling overlapping appointments, entering nonexistent or invalid customer data, entering an incorrect username and password
- usage of lambda expressions to make program more efficient
- alert if there is an appointment within 15 minutes of the user’s log-in.
- ability to generate the following reports: number of appointment types by month, the schedule for each consultant
- ability to track user activity by recording timestamps for user log-ins in a .txt file. Each new record is appended to the log file, if the file already exists.
