use desafioVotacao
db.getCollection("pauta").insertOne({
    "_id" : ObjectId("6110e0129df3a5a55f6df739"),
    "title" : "Título da Pauta",
    "description" : "Descrição da Pauta",
    "dateStart" : ISODate("2024-02-21T15:30:00.000+0000"),
    "dateEnd" : ISODate("2024-02-21T15:31:00.000+0000"),
    "votos" : [
        {
            "_id" : ObjectId("6110e0129df3a5a55f6df73a"),
            "username" : "Usuário 1",
            "voto" : true,
            "dateStart" : ISODate("2024-02-21T15:30:00.000+0000"),
            "pautaId" : ObjectId("6110e0129df3a5a55f6df739"),
            "usuario" : {
                "_id" : ObjectId("6110e0129df3a5a55f6df73a"),
                "name" : "glauber",
                "cpf" : "15631960770",
                "email" : "gabriel@gmail.com",
                "password" : "123456789"
            }
        }
    ],
    "ativated" : true
})
    db.getCollection("usuario").insertOne({
    
        
        "name":"teste",
        "cpf":"945.096.810-11",
        "email":"teste@gmail.com",
        "password":"123456789"
    })

