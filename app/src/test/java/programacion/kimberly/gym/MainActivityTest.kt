package programacion.kimberly.gym

import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import programacion.kimberly.gym.register.MainActivity
import programacion.kimberly.gym.register.RegisterActivity
import programacion.kimberly.gym.register.ResetPasswordActivity

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class MainActivityTest {

    private lateinit var activity: MainActivity
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var forgotPasswordTextView: TextView

    @Before
    fun setUp() {
        // Inicializar la actividad bajo prueba
        activity = Robolectric.buildActivity(MainActivity::class.java).create().resume().get()
        emailEditText = activity.findViewById(R.id.editTextEmail)
        passwordEditText = activity.findViewById(R.id.editTextPassword)
        loginButton = activity.findViewById(R.id.buttonLogin)
        registerButton = activity.findViewById(R.id.buttonRegister)
        forgotPasswordTextView = activity.findViewById(R.id.textViewForgotPassword)
    }

    @After
    fun tearDown() {
        // Finalizar la actividad después de cada prueba
        activity.finish()
    }

    @Test
    fun testLoginButton() {
        // Verificar que la actividad se haya iniciado correctamente
        assertNotNull(activity)

        // Establecer el texto del campo de correo electrónico y contraseña
        emailEditText.setText("example@example.com")
        passwordEditText.setText("password")

        // Realizar clic en el botón de inicio de sesión
        loginButton.performClick()

        // Verificar que se inicie la actividad FirstActivity después de iniciar sesión
        val expectedIntent = Intent(activity, FirstActivity::class.java)
        val actualIntent = shadowOf(RuntimeEnvironment.application).nextStartedActivity
        assertEquals(expectedIntent.component, actualIntent.component)
    }

    @Test
    fun testRegisterButton() {
        // Verificar que la actividad se haya iniciado correctamente
        assertNotNull(activity)

        // Realizar clic en el botón de registro
        registerButton.performClick()

        // Verificar que se inicie la actividad RegisterActivity después de hacer clic en el botón de registro
        val expectedIntent = Intent(activity, RegisterActivity::class.java)
        val actualIntent = shadowOf(RuntimeEnvironment.application).nextStartedActivity
        assertEquals(expectedIntent.component, actualIntent.component)
    }

    @Test
    fun testForgotPasswordTextView() {
        // Verificar que la actividad se haya iniciado correctamente
        assertNotNull(activity)

        // Realizar clic en el texto de olvidó la contraseña
        forgotPasswordTextView.performClick()

        // Verificar que se inicie la actividad ResetPasswordActivity después de hacer clic en el texto de olvidó la contraseña
        val expectedIntent = Intent(activity, ResetPasswordActivity::class.java)
        val actualIntent = shadowOf(RuntimeEnvironment.application).nextStartedActivity
        assertEquals(expectedIntent.component, actualIntent.component)
    }
}
