package desafio.encurtadorurl

import com.mongodb.MongoClientURI
import com.mongodb.MongoException
import org.litote.kmongo.*

class EncurtadorUrlController {
    val mongoUri = MongoClientURI("mongodb://172.18.0.2:27017/encurtadorUrl")
    val col = KMongo.createClient(uri = mongoUri).getDatabase(mongoUri.database!!).getCollection<EncurtadorUrlModel>()

    @Throws(MongoException::class)
    fun saveHashFromUrl(hash: String, url: String) {
        col.insertOne(EncurtadorUrlModel(hash, url))
    }

    @Throws(MongoException::class)
    fun getUrlByHash(hash: String): String? {
        return col.findOne(EncurtadorUrlModel::hash eq hash)?.original
    }

    @Throws(MongoException::class)
    fun getAllSavedUrls(): List<EncurtadorUrlModel> {
        return col.find("{}").toMutableList()
    }
}