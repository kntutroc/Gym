package programacion.kimberly.gym

import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [30], manifest = Config.NONE)
class NextRegisterActivityTest {

    @Mock
    private lateinit var firestoreMock: FirebaseFirestore

    private lateinit var activity: NextRegisterActivity

    @Before
    fun setup() {
        // Inicializa las simulaciones de Mockito
        firestoreMock = Mockito.mock(FirebaseFirestore::class.java)

        // Inicializa la actividad
        activity = NextRegisterActivity()
        activity.firestore = firestoreMock
        activity.onCreate(null)
    }

    @Test
    fun testSavePersonalInformation_Success() {
        activity.editTextFirstName.setText("John")
        activity.editTextLastName.setText("Doe")
        activity.editTextPhoneNumber.setText("1234567890")
        activity.editTextBirthYear.setText("1990")
        activity.editTextHeight.setText("175.5")
        activity.editTextWeight.setText("70.5")
        `when`(activity.radioGroupGender.checkedRadioButtonId).thenReturn(R.id.radioButtonMale)

        activity.savePersonalInformation("test_user_uid")

        // Verifica que se haya llamado a FirebaseFirestore correctamente
        verify(firestoreMock).collection(any<String>()).document(any<String>())
            .set(any<HashMap<String, Any>>())
    }

    @Test
    fun testSavePersonalInformation_Failure() {
        activity.editTextFirstName.setText("Jane")
        activity.editTextLastName.setText("Doe")
        activity.editTextPhoneNumber.setText("1234567890")
        activity.editTextBirthYear.setText("1995")
        activity.editTextHeight.setText("160")
        activity.editTextWeight.setText("33")

        // Ejecuta el método bajo prueba
        activity.savePersonalInformation("test_user_uid")

        // Verifica que no se haya llamado a FirebaseFirestore debido a la falla en la validación
        verify(firestoreMock, never()).collection(any<String>()).document(any<String>())
            .set(any<HashMap<String, Any>>())
    }
}
