package com.groupe2.microservicedataobject;

import com.groupe2.microservicedataobject.dataobject.DataObjectAlreadyExists;
import com.groupe2.microservicedataobject.dataobject.DataObjectNotFoundException;
import com.groupe2.microservicedataobject.dataobject.IDataObject;
import com.groupe2.microservicedataobject.dataobject.PathContainsOtherObjectsException;
import com.groupe2.microservicedataobject.dataobject.aws.AwsBucketHelper;
import com.groupe2.microservicedataobject.dataobject.aws.AwsDataObject;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectDataTest {

    static final String ROOT = "amt.team02.diduno.education";
    static final String IMAGE_KEY = "AwUDAwMDAwYEBAMFBwYHBwcGBw";
    static final String ALWAYS_EXIST_KEY = "ajdajsdljasdjpasjd";
    static final String ALWAYS_NOT_EXIST_KEY = "AKeyThatDoesNotExists";
    static final IDataObject ALWAYS_EXIST_OBJECT = new AwsDataObject(ROOT + "/" + ALWAYS_EXIST_KEY);

    static private final String B64_IMAGE = "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";

    @BeforeAll
    static void setup() {
        createRoot();

        if (!ALWAYS_EXIST_OBJECT.exists()) {
            ALWAYS_EXIST_OBJECT.upload(B64_IMAGE);
        }
    }

    @AfterAll
    static void tearDown() {
        createRoot();
    }

    static void createRoot() {
        if (!AwsBucketHelper.bucketExists(ROOT)) {
            AwsBucketHelper.createBucket(ROOT);
            ALWAYS_EXIST_OBJECT.upload(B64_IMAGE);
        }
    }

    @AfterEach
    void cleanUp() {
        IDataObject object = new AwsDataObject(ROOT + "/" + IMAGE_KEY);
        try {
            object.delete(true);
        } catch (Exception e) {
            // do nothing
        }
    }

    @Test
    void objectShouldBeDeleted() {
        IDataObject object = new AwsDataObject(ROOT + "/" + IMAGE_KEY);
        object.upload(B64_IMAGE);
        assertTrue(object.exists());
        object.delete();
        assertFalse(object.exists());
    }

    @Test
    void DoesObjectExist_RootObjectExists_Exists() {
        //given
        IDataObject object = new AwsDataObject(ROOT);

        //when
        //then
        assertTrue(object.exists());
    }

    @Test
    void DoesObjectExist_RootObjectDoesntExist_DoesntExist() {
        //given
        IDataObject object = new AwsDataObject("TestRoot");

        //when
        //then
        assertFalse(object.exists());
    }

    @Test
    void DoesObjectExist_RootObjectAndObjectExist_Exists() {
        //given
        boolean exist = ALWAYS_EXIST_OBJECT.exists();
        //when
        //then
        assertTrue(exist);
    }

    @Test
    void DoesObjectExist_RootObjectExistObjectDoesntExist_DoesntExist() {
        //given
        IDataObject object = new AwsDataObject(ROOT + "/" + ALWAYS_NOT_EXIST_KEY);

        //when
        //then
        assertFalse(object.exists());
    }

    @Test
    void UploadObject_RootObjectExistsNewObject_Uploaded() {
        //given
        IDataObject object = new AwsDataObject(ROOT + "/" + IMAGE_KEY);

        //when
        object.upload(B64_IMAGE);

        //then
        assertTrue(object.exists());
    }

    @Test
    void UploadObject_RootObjectExistsObjectAlreadyExists_ThrowException() {
        //given
        //when
        //then
        assertThrows(DataObjectAlreadyExists.class, () -> ALWAYS_EXIST_OBJECT.upload(B64_IMAGE));
    }

    @Test
    @Disabled
    void UploadObject_RootObjectDoesntExist_Uploaded() {
        try{
            //given
            IDataObject root = new AwsDataObject(ROOT);
            IDataObject object = new AwsDataObject(ROOT + "/" + IMAGE_KEY);

            //when
            root.delete(true);
            object.upload(B64_IMAGE);

            //then
            assertTrue(object.exists());
        }finally {
            setup();
        }

    }

    @Test
    void DownloadObject_ObjectExists_Downloaded() throws Exception {
        //given
        byte[] bytes = ALWAYS_EXIST_OBJECT.download();

        //when
        //then
        assertNotEquals(bytes.length, 0);
    }

    @Test
    void DownloadObject_ObjectDoesntExist_ThrowException() {
        //given
        IDataObject object = new AwsDataObject(ROOT + "/" + ALWAYS_NOT_EXIST_KEY);

        //when
        //then
        assertThrows(DataObjectNotFoundException.class, object::download);
    }

    @Test
    void PublishObject_ObjectExists_Published() {
        //given
        String url = ALWAYS_EXIST_OBJECT.getUrl();
        //when
        //then
        assertNotNull(url);
    }

    @Test
    void PublishObject_ObjectDoesntExist_ThrowException() {
        //given
        IDataObject notExistingObject = new AwsDataObject(ROOT + "/" + ALWAYS_NOT_EXIST_KEY);
        //when
        //then
        assertThrows(DataObjectNotFoundException.class, notExistingObject::getUrl);
    }

    @Test
    void RemoveObject_SingleObjectExists_Removed() {
        //given
        IDataObject object = new AwsDataObject(ROOT + "/" + IMAGE_KEY);
        //when
        object.upload(B64_IMAGE);
        boolean existBefore = object.exists();
        object.delete();
        boolean existAfter = object.exists();
        //then
        assertFalse(existAfter);
        assertTrue(existBefore);
    }

    @Test
    void RemoveObject_SingleObjectDoesntExist_ThrowException() {
        //given
        IDataObject object = new AwsDataObject(ROOT + "/" + ALWAYS_NOT_EXIST_KEY);
        //when
        //then
        assertThrows(DataObjectNotFoundException.class, object::delete);
    }

    @Test
    void RemoveObject_FolderObjectExistWithoutRecursiveOption_ThrowException() {

        final String testFolder = "testFolder";
        //given
        IDataObject object1 = new AwsDataObject(ROOT + "/" + testFolder + "/object1");
        IDataObject object2 = new AwsDataObject(ROOT + "/" + testFolder + "/object2");
        IDataObject folder = new AwsDataObject(ROOT + "/" + testFolder);

        try {
            //when
            object1.upload(B64_IMAGE);
            object2.upload(B64_IMAGE);
            boolean object1Exists = object1.exists();
            boolean object2Exists = object2.exists();
            //then
            assertTrue(object1Exists);
            assertTrue(object2Exists);
            assertThrows(PathContainsOtherObjectsException.class, folder::delete);

        } finally {
            object1.delete();
            object2.delete();
        }

    }

    @Test
    void RemoveObject_FolderObjectExistWithRecursiveOption_Removed() {
        final String testFolder = "testFolder";
        //given
        IDataObject object1 = new AwsDataObject(ROOT + "/" + testFolder + "/object1");
        IDataObject object2 = new AwsDataObject(ROOT + "/" + testFolder + "/object2");
        IDataObject folder = new AwsDataObject(ROOT + "/" + testFolder);
        //when
        object1.upload(B64_IMAGE);
        object2.upload(B64_IMAGE);
        boolean object1Exists = object1.exists();
        boolean object2Exists = object2.exists();
        folder.delete(true);
        boolean object1ExistsAfter = object1.exists();
        boolean object2ExistsAfter = object2.exists();
        //then
        assertTrue(object1Exists);
        assertTrue(object2Exists);
        assertFalse(object1ExistsAfter);
        assertFalse(object2ExistsAfter);
        assertFalse(folder.exists());
    }

    @Test
    @Disabled
    void RemoveObject_RootObjectNotEmptyWithoutRecursiveOption_ThrowException() {
        try {
            //given
            IDataObject object = new AwsDataObject(ROOT);
            //when
            //then
            assertThrows(PathContainsOtherObjectsException.class, object::delete);
        } finally {
            setup();
        }

    }

    @Test
    @Disabled
    void RemoveObject_RootObjectNotEmptyWithRecursiveOption_Removed() {
        try {
            //given
            IDataObject object = new AwsDataObject(ROOT);
            //when
            object.delete(true);
            //then
            assertFalse(object.exists());
        } finally {
            setup();
        }

    }
}
