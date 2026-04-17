db.beneficiaries.insertMany([
    {
        _id: ObjectId(),
        userId: "69e14d79732348491ab53963",
        name: "Shriya Savings",
        accountNumber: "69e28be7ec1161a31639b30c",
        createdAt: new Date(),
        updatedAt: new Date(),
        _class: "com.fraudplatform.user_account_service.model.Beneficiary"
    },
    {
        _id: ObjectId(),
        userId: "69e14d79732348491ab53963",
        name: "Shriya Checking",
        accountNumber: "69e28be7ec1161a31639b30d",
        createdAt: new Date(),
        updatedAt: new Date(),
        _class: "com.fraudplatform.user_account_service.model.Beneficiary"
    }
])