package dan.nr.first_project.database.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("notes")
data class Note(
    val title: String,
    val content: String,
    val color: Long,
    val createdAt: Instant,
    //Id of the user who created the note.
    val ownerId: ObjectId,
    @Id val id: ObjectId = ObjectId.get()
)