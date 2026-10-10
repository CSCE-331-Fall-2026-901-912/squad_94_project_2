# Table of Contents

- [About](#about)

# About

This repo contains all assets and source code for Squad 94's Project 2 in Texas A&M University's CSCE 331 course.

Squad 94 is comprised of:
- Rigo Chiti
- Mairead Finnerty
- Sanjana Ram
- Natalie Gonzalez
- Asher Blevins

The generated orders.csv file currently lists $1,002,544.61 worth of sales made from September 30th, 2025 to September 30th, 2026.

There are three peak days of notably higher numbers of orders made. These days are:
August 24th, 2026 -- First day of Texas A&M semester.
May 4th, 2026 -- First day of finals of Spring 2026 semester.
May 5th, 2026 -- Second day of finals of Spring 2026 semester.

# Running the Application

To run the application from terminal, you need to cd into where you downloaded the squad_94_project_2 directory.
Copy the "COPY_THEN_MODIFY_ME.cmd" file. Rename the copy to "run.cmd".
Open the run.cmd file, and fill in the path argument for each occurrence of the --module-path parameter with your local installation of a JavaFX sdk version 26.
(Replace each instance of ".YOUR\OWN\PATH\TO\javafx-sdk-26\lib" with a path to the JavaFX sdk 26.)
Locate DATABASE_PASSWORD_HERE at the end of run.cmd. Change this to the database password for the database this program connects to.

After this, simply type in your terminal: 

### .\run.cmd
###
This should immediately boot the application and all of its dependencies. To exit the application, simply click the red X in the upper right corner of each currently open window.

Do not expect perfect performance for the application if you have X'd out of any of the dependant windows, as this may break the logic in the backend.
