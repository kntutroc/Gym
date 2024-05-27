package programacion.kimberly.gym

import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import programacion.kimberly.gym.register.MainActivity
import programacion.kimberly.gym.register.ResetPasswordActivity

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ResetPasswordActivityTest {

    private lateinit var activity: ResetPasswordActivity
    private lateinit var editTextEmail: EditText
    private lateinit var buttonResetPassword: Button

    @Before
    fun setUp() {
        // Inicializar la actividad bajo prueba
        activity = Robolectric.buildActivity(ResetPasswordActivity::class.java).create().resume().get()
        editTextEmail = activity.findViewById(R.id.editTextEmail)
        buttonResetPassword = activity.findViewById(R.id.buttonResetPassword)
    }

    @After
    fun tearDown() {
        // Finalizar la actividad después de cada prueba
        activity.finish()
    }

    @Test
    fun testResetPasswordButton() {
        // Verificar que la actividad se haya iniciado correctamente
        assertNotNull(activity)

        // Establecer el texto del campo de correo electrónico
        editTextEmail.setText("example@example.com")

        // Realizar clic en el botón de restablecimiento de contraseña
        buttonResetPassword.performClick()

        // Verificar que se inicie la actividad MainActivity después de enviar el restablecimiento de contraseña
        val expectedIntent = Intent(activity, MainActivity::class.java)
        val actualIntent = Shadows.shadowOf(activity).nextStartedActivity
        assertEquals(expectedIntent.component, actualIntent.component)

        // Capturar el Toast mostrado
        val toast = Shadows.shadowOf(Toast.makeText(RuntimeEnvironment.application, "", Toast.LENGTH_SHORT))

    }

    @Test
    fun testEmptyEmail() {
        // Verificar que la actividad se haya iniciado correctamente
        assertNotNull(activity)

        // Establecer el campo de correo electrónico vacío
        editTextEmail.setText("")

        // Realizar clic en el botón de restablecimiento de contraseña
        buttonResetPassword.performClick()

        // Capturar el Toast mostrado
        val toast = Shadows.shadowOf(Toast.makeText(RuntimeEnvironment.application, "", Toast.LENGTH_SHORT))

    }
}
