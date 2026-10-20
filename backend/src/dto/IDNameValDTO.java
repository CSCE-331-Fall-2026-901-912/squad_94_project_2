package dto;

/**
 * Generic data transfer object to store related data which fits into an ID-name-value scheme.
 * The idea is that the ID-name-value represents some discrete data piece.
 * DTO enables extraction of data from the structure and creation and transfer of the structure.
 * Informally, one can call this a 3-tuple (int, String, int).
 *
 * @author Squad 94 - Rigo Chiti
 * @param id The identifier associated with the discrete data piece.
 * @param name The name associated with the discrete data piece.
 * @param val The value associate with the discrete data piece.
 */
// General-purpose (generic) DTO for recording and quickly transferring any information with an ID-Name-AssociatedValue paradigm.
public record IDNameValDTO(int id, String name, int val) {}