# spring-ppd-rest-api
This repository is an interview exercise implemented in Spring Boot.

## The original description

We would like you to develop a REST API for a fictional client “FooBar Inc”. FooBar Inc need a
REST API for accessing House Price Paid Data (PPD).
The raw data can be found, for free, as a CSV file here:
https://www.gov.uk/government/statistical-data-sets/price-paid-data-downloads.

### The Story
As a developer at FooBar Inc.
I want to access house price paid records in JSON format via a REST API
So that I can build an automated system using this data
Acceptance Criteria
- A list of all records is returned in JSON format via the REST API
- A single record is returned in JSON format when its ID is provided
- A list of purchase records made in a specified time range is returned in JSON format
when a date time range is provided

## Solution
This section is aiming to describing the technical analysis of the task. 
 
### Discovery of the source data
Data of House Price Paid records are available in CSV format. From the www.gov.uk we can download the following data:

- Current Monthly subset of PPD ( [link](http://prod.publicdata.landregistry.gov.uk.s3-website-eu-west-1.amazonaws.com/pp-monthly-update-new-version.csv) )
- Full year PPD (e.g: 2021 -> [link](http://prod.publicdata.landregistry.gov.uk.s3-website-eu-west-1.amazonaws.com/pp-2021.csv) )
- Half year of PPD data ( e.g: 2021 part1 -> [link](http://prod.publicdata.landregistry.gov.uk.s3-website-eu-west-1.amazonaws.com/pp-2021-part1.csv) )
- All the registered PPD data since 2018 ( [link](http://prod.publicdata.landregistry.gov.uk.s3-website-eu-west-1.amazonaws.com/pp-complete.csv) )

The CSV record field descriptions are available here: https://www.gov.uk/guidance/about-the-price-paid-data?fbclid=IwAR0FxqBOGDmWEYAF4vb106jzuKyKroj419qMkx_YFcmo4MHhn-aP_Nbvdnk

### CSV handling

The available CSV files above are huge, therefore handling them and exposing in REST API required some advanced technical skills.
In this implementation I have solved the CSV handling by evaluating records one by one via Java Streams, and used API response paging described in Paging & HATEOAS section.

## Usage of the PPD API

Swagger and Swagger UI also implemented in the API and available under ``` http://<host>:<port>/swagger-ui/ ```

### Build

After cloning this repository you can build the application the same way as a regular maven project.

Just like every Spring Boot application, you can compile the executable (fat) jar out of the source code.
To build the executable jar you need to run the following maven goal:

```
mvn clean install
```

After the goal finished you should see the executable jar in the /target folder (e.g.: ppd-spring-rest-api-1.0.0-RELEASE.jar)

### Run

The previously built jar is executable from CLI with the following command:

```
java -jar ppd-spring-rest-api-1.0.0-RELEASE.jar
```

By default the API is running on localhost at port 8080. To specify the port you have to override the Tomcat default property like this:

```
java -jar ppd-spring-rest-api-1.0.0-RELEASE.jar --server.port=<port_number_of_your_choice>
```

### Repository options

Based on the source data analysis I have provided two different backend approach.

1. #### Real-Time CSV proxy: 
   
   With this repository you are able to download and query the csv available at http://prod.publicdata.landregistry.gov.uk.s3-website-eu-west-1.amazonaws.com/pp-monthly-update.txt. 
   
   In case of using real-time repository every API request will download the csv above and do the filtering on the downloaded result. To use Real-Time repository to have to set up the ```PricePaidDataRepository``` Bean like this:
   
   ```java
   @SpringBootApplication
   public class PpdSpringRestApiApplication {
   
   	public static void main(String[] args) {
   		SpringApplication.run(PpdSpringRestApiApplication.class, args);
   	}
   
       @Bean
       public PricePaidDataRepository dataRepository(){
           return new RealTimeCsvPricePaidDataRepository();
       }
   
   }
   ```
   
2. #### File system cached repository:
    
    This repository is designed to handle big CSV files stored in the local filesystem. To use this repository you have to set the location of the CSV file via the ```data.ppd.localUri```  property.
    This can be run by simply overriding the property in the run command: 
   ```
   java -jar ppd-spring-rest-api-1.0.0-RELEASE.jar --data.ppd.localUri=<path_to_csv_file>
   ```
   With this repository you can download and store all the PPD data available at www.gov.uk.
   To use File system cached repository to have to set up the ```PricePaidDataRepository``` Bean like this:
   ```java
   @SpringBootApplication
   public class PpdSpringRestApiApplication {
   
   	public static void main(String[] args) {
   		SpringApplication.run(PpdSpringRestApiApplication.class, args);
   	}
   
       @Bean
       public PricePaidDataRepository dataRepository(){
           return new FileSystemCachedCsvPricePaidDataRepository();
       }
   }
   ```

### Paging & HATEOAS

The CSV database is too big to serve the whole content on the endpoint in one HTTP response. To solve this issue, the API is using giving back only a subset of the CSV (pages) and providing links to the next and previous subsets.
This is behaviour is called paging in this terminology. The paging implementation follows the HATEOAS principles. In advance every PPD record contains links to itself, which can be useful to create reactive frontend for the API. For example requesting the all records will give you the following result:

```json
{
    "pagingDetails": {
        "pageNumber": 1,
        "startRecord": 1,
        "endRecord": 3,
        "_links": {
            "nextPage": {
                "href": "http://localhost:8080/ppd/?pageNo=2"
            }
        }
    },
    "pricePaidData": [
        {
            "transactionUniqueIdentifier": "5BBE9CB3-6332-4EB0-9CD3-8737CEA4A65A",
            "price": 42000,
            "dateOfTransfer": "1995-12-21T00:00:00",
            "postcode": "NE4 9DN",
            "propertyType": "S",
            "oldNew": false,
            "duration": "F",
            "paon": "8",
            "saon": "",
            "street": "MATFEN PLACE",
            "locality": "FENHAM",
            "city": "NEWCASTLE UPON TYNE",
            "district": "NEWCASTLE UPON TYNE",
            "county": "TYNE AND WEAR",
            "ppdCategory": "A",
            "recordStatus": "A",
            "_links": {
                "self": {
                    "href": "http://localhost:8080/ppd/5BBE9CB3-6332-4EB0-9CD3-8737CEA4A65A"
                }
            }
        },
        {
            "transactionUniqueIdentifier": "20E2441A-0F16-49AB-97D4-8737E62A5D93",
            "price": 95000,
            "dateOfTransfer": "1995-03-03T00:00:00",
            "postcode": "RM16 4UR",
            "propertyType": "S",
            "oldNew": false,
            "duration": "F",
            "paon": "30",
            "saon": "",
            "street": "HEATH ROAD",
            "locality": "GRAYS",
            "city": "GRAYS",
            "district": "THURROCK",
            "county": "THURROCK",
            "ppdCategory": "A",
            "recordStatus": "A",
            "_links": {
                "self": {
                    "href": "http://localhost:8080/ppd/20E2441A-0F16-49AB-97D4-8737E62A5D93"
                }
            }
        },
        {
            "transactionUniqueIdentifier": "F9F753A8-E56A-4ECC-9927-8E626A471A92",
            "price": 43500,
            "dateOfTransfer": "1995-11-14T00:00:00",
            "postcode": "TS23 3LA",
            "propertyType": "S",
            "oldNew": false,
            "duration": "F",
            "paon": "19",
            "saon": "",
            "street": "SLEDMERE CLOSE",
            "locality": "BILLINGHAM",
            "city": "BILLINGHAM",
            "district": "STOCKTON-ON-TEES",
            "county": "STOCKTON-ON-TEES",
            "ppdCategory": "A",
            "recordStatus": "A",
            "_links": {
                "self": {
                    "href": "http://localhost:8080/ppd/F9F753A8-E56A-4ECC-9927-8E626A471A92"
                }
            }
        }
    ]
}
```

Size of the pages is 100 record by default, but it you can override via the ```data.ppd.pageSize ``` application property like this:
```
   java -jar ppd-spring-rest-api-1.0.0-RELEASE.jar --data.ppd.pageSize=<size_of_your_choice>
```

    
### Future improvements

- [ ] Repository change wiring to application property
- [ ] Debug logging
- [ ] Maintaining current number of records on CSV
- [ ] Extend with ETL application (via Spring Batch) to keep the filesystem CSV up to date
- [ ] Implement API security (OAuth Client Credentials and/or API-Key & Secret)
- [ ] Implement indexing feature to decrease random access response time