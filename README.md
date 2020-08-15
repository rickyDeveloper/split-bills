# split-bills

Objective:-

1) Capture the bill payments of N persons
2) Calculate the total amount each person has to other person
3) Reduce the total number of transactions.



How to test:-

1) Start the spring boot application from BillsApplication.java .   
2) RestApi BillsController provides "/bills" to add bill payments.  POST http://localhost:8080/bills
3) "/summary" provides the reduced transaction summary.   GET http://localhost:8080/summary

Sample payload for POST "/bills" api 

{
    "contributionDTOS": [
        {
            "personName": "B",
            "liableAmount": 0,
            "paidAmount": 20
        },
        {
            "personName": "A",
            "liableAmount": 20,
            "paidAmount": 0
        },
        {
            "personName": "C",
            "liableAmount": 0,
            "paidAmount": 0
        },
        {
            "personName": "D",
            "liableAmount": 0,
            "paidAmount": 0
        }
    ]
}


Algorithm:-

1) Created a Graph to capture the bill payment and amount due by each person 
2) Used a customized Breadth First Traversal technique to reduce the transaction count. 
