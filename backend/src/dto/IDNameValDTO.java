package dto;

// General-purpose (generic) DTO for recording and quickly transferring any information with an ID-Name-AssociatedValue paradigm.
public record IDNameValDTO(int id, String name, int val) {}