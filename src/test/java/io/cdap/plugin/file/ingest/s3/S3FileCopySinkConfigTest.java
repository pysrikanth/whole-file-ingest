/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.plugin.file.ingest.s3;

import io.cdap.cdap.etl.mock.validation.MockFailureCollector;
import io.cdap.plugin.common.Constants;
import io.cdap.plugin.file.ingest.ValidationAssertions;
import org.junit.Assert;
import org.junit.Test;

public class S3FileCopySinkConfigTest {
  private static final String MOCK_STAGE = "mockStage";
  private static final S3FileCopySinkConfig VALID_CONFIG = new S3FileCopySinkConfig(
    "ref",
    "",
    false,
    false,
    null,
    "s3a://asd",
    "",
    ""
  );

  @Test
  public void testValidConfig() {
    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    VALID_CONFIG.validate(failureCollector);
    Assert.assertTrue(failureCollector.getValidationFailures().isEmpty());
  }

  @Test
  public void testValidateReference() {
    S3FileCopySinkConfig config = S3FileCopySinkConfig.builder(VALID_CONFIG)
      .setReferenceName("")
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    ValidationAssertions.assertPropertyValidationFailed(failureCollector, Constants.Reference.REFERENCE_NAME);
  }

  @Test
  public void testValidateBufferSizeNonPositiveValue() {
    S3FileCopySinkConfig config = S3FileCopySinkConfig.builder(VALID_CONFIG)
      .setBufferSize(0)
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    ValidationAssertions.assertPropertyValidationFailed(failureCollector, S3FileCopySinkConfig.BUFFER_SIZE);
  }

  @Test
  public void testValidateSchema() {
    S3FileCopySinkConfig config = S3FileCopySinkConfig.builder(VALID_CONFIG)
      .setFilesystemURI("q://asd")
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    ValidationAssertions.assertPropertyValidationFailed(failureCollector, S3FileCopySinkConfig.SCHEME);
  }
}
