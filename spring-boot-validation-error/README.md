# How to Align Spring Boot Validation Errors with Your JSON Property Naming Strategy

You can reach the blog post [here](https://medium.com/@ibrahimgunduz34/how-to-align-spring-boot-validation-errors-with-your-json-property-naming-strategy-f200c3afcde7)

## How To Test It
* Start the application
* Send the following request
```shell
curl -XPOST "http://localhost:8080/merchants" \
-H "Content-Type: application/json" \
-d "{}"                                                    
```

The API would respond with the following content
```json
{
  "errors" : [ 
    "merchantCreateRequest.partner_id: must not be null", 
    "merchantCreateRequest.merchant_name: must not be null" 
  ]
}
```
