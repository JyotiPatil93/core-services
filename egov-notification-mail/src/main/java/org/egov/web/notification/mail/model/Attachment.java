/**
 * 
 */
package org.egov.web.notification.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author TapojitB
 * Attachment Related Enhancement, This class represents a single file attachment.
 *
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
public class Attachment {
	private String fileName;// Name of the file
	private String fileType;// Type of the file, e.g."application/pdf"
	private byte[] fileContent; // Content stream of the file

}
