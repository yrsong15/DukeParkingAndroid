# Duke Parking Android
Android application built with PostgreSQL that tracks number of open spots for each parking lot on Duke University campus. 

### Members (in alphabetic order)
+ Helen Lee (hl148@duke.edu)
+ Ray Song (ys101@duke.edu)
+ Kaz Takabayashi (kbt8@duke.edu)
+ Adriaan Venter (ajv9@duke.edu)
+ Harry Wu (yw141@duke.edu)

### Database
The raw data, which is provided as a daily dump from *Duke Parking*, contains an aggregation of all parking-related data from  a given day. This raw data is then processed with a **macro script** in order to refine values and project only the relevant columns. The results of the macro are stored in an intermediate CSV-format file.

The intermediate file is then processed with a SQL query in **PostgreSQL**. The query reorganizes the database so that the result is grouped by each parking lot on an hourly basis, with the number of cars that are in each parking lot at the given hour. This result is stored in a three-column format: (ParkingLotNumber, NumberOfCars, and Hour). 
  + For example, the column (2001, 426, 22) would mean that there were 426 cars between 9-10PM in Parking Lot #2001.
  
The results of the SQL query are stored in a final CSV-format file, which is then passed onto a cloud server for the frontend to process.
