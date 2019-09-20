package milos.zelko.grtest.model

data class User (
    private var id: Long,
    private var email: String,
    private var firstName: String,
    private var lastName: String,
    private var avatar: String
)