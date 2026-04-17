db.accounts.insertMany([
    {
        _id: ObjectId(),
        userId: "69e14d79732348491ab53963",
        accountType: "SAVINGS",
        balance: NumberDecimal("5000.00"),
        createdAt: new Date(),
        updatedAt: new Date(),
        active: true,
        _class: "com.fraudplatform.user_account_service.model.Account"
    },
    {
        _id: ObjectId(),
        userId: "69e14d79732348491ab53963",
        accountType: "CHECKING",
        balance: NumberDecimal("2500.50"),
        createdAt: new Date(),
        updatedAt: new Date(),
        active: true,
        _class: "com.fraudplatform.user_account_service.model.Account"
    },
    {
        _id: ObjectId(),
        userId: "69e14d79732348491ab53963",
        accountType: "CURRENT",
        balance: NumberDecimal("10000.00"),
        createdAt: new Date(),
        updatedAt: new Date(),
        active: true,
        _class: "com.fraudplatform.user_account_service.model.Account"
    }
])

db.accounts.insertMany([
    {
        _id: ObjectId(),
        userId: "69e28b909c69340881449d06",
        accountType: "SAVINGS",
        balance: NumberDecimal("8750.25"),
        createdAt: new Date(),
        updatedAt: new Date(),
        _class: "com.fraudplatform.user_account_service.model.Account"
    },
    {
        _id: ObjectId(),
        userId: "69e28b909c69340881449d06",
        accountType: "CHECKING",
        balance: NumberDecimal("1200.75"),
        createdAt: new Date(),
        updatedAt: new Date(),
        _class: "com.fraudplatform.user_account_service.model.Account"
    },
    {
        _id: ObjectId(),
        userId: "69e28b909c69340881449d06",
        accountType: "CURRENT",
        balance: NumberDecimal("25000.00"),
        createdAt: new Date(),
        updatedAt: new Date(),
        _class: "com.fraudplatform.user_account_service.model.Account"
    },
    {
        _id: ObjectId(),
        userId: "69e28b909c69340881449d06",
        accountType: "JOINT",
        balance: NumberDecimal("4320.50"),
        createdAt: new Date(),
        updatedAt: new Date(),
        _class: "com.fraudplatform.user_account_service.model.Account"
    }
])