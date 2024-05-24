package programacion.kimberly.gym

import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30], manifest = Config.NONE)
class RegisterActivityTest {

    private lateinit var activity: RegisterActivity
    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockAuthResult: AuthResult
    private lateinit var mockFirebaseUser: FirebaseUser

    @Before
    fun setUp() {
        activity = Robolectric.buildActivity(RegisterActivity::class.java).create().get()
        mockAuth = mockk(relaxed = true)
        mockAuthResult = mockk(relaxed = true)
        mockFirebaseUser = mockk(relaxed = true)

        activity.auth = mockAuth
    }

    @Test
    fun testCreateAccount_Success() {
        val email = "test@example.com"
        val password = "password123"

        // Simular la entrada del usuario
        activity.findViewById<EditText>(R.id.editTextEmail).setText(email)
        activity.findViewById<EditText>(R.id.editTextPassword).setText(password)

        // Simular una respuesta exitosa de Firebase
        every { mockAuth.createUserWithEmailAndPassword(email, password) } returns mockk {
            addOnCompleteListener(activity) { task ->
                every { task.isSuccessful } returns true
                every { mockAuth.currentUser } returns mockFirebaseUser
                every { mockFirebaseUser.uid } returns "mockUid"
            }
        }

        // Hacer clic en el botón de registro
        activity.findViewById<Button>(R.id.buttonNext).performClick()

        // Verificar la intención de iniciar la siguiente actividad
        val expectedIntent = Intent(activity, NextRegisterActivity::class.java)
        val actualIntent = Shadows.shadowOf(activity).nextStartedActivity
        assertEquals(expectedIntent.component, actualIntent.component)

        // Verificar que el UID se pasa correctamente
        assertEquals("mockUid", actualIntent.extras?.getString("USER_UID"))

        // Verificar que la actividad actual se cierra
        assertTrue(activity.isFinishing)
    }

    @Test
    fun testCreateAccount_Failure() {
        val email = "test@example.com"
        val password = "password123"

        // Simular la entrada del usuario
        activity.findViewById<EditText>(R.id.editTextEmail).setText(email)
        activity.findViewById<EditText>(R.id.editTextPassword).setText(password)

        // Simular una respuesta fallida de Firebase
        every { mockAuth.createUserWithEmailAndPassword(email, password) } returns mockk {
            addOnCompleteListener(activity) { task ->
                every { task.isSuccessful } returns false
                every { task.exception } returns Exception("Authentication failed")
            }
        }

        // Hacer clic en el botón de registro
        activity.findViewById<Button>(R.id.buttonNext).performClick()

        // Verificar que se muestra el mensaje de error
        assertEquals("Authentication failed: Authentication failed", ShadowToast.getTextOfLatestToast())
    }
}
