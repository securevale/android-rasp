include!("trusted_signers_data.rs");

#[inline(always)]
fn concealed(value: &u8) -> u8 {
    // Volatile reads stop LTO from folding the arrays back into a plaintext digest.
    // SAFETY: `value` is always a valid reference to an initialized byte in one of
    // the fixed-size static arrays generated in `trusted_signers_data.rs`.
    unsafe { std::ptr::read_volatile(value) }
}

/// Compares a SHA-256 digest against every configured trusted signer.
///
/// The complete expected digest is reconstructed only while each byte is compared.
#[inline(never)]
pub fn matches(candidate: &[u8]) -> bool {
    if candidate.len() != 32 {
        return false;
    }

    let mut matched = 0u8;
    for encoded in &ENCODED {
        let mut difference = 0u8;
        for index in 0..32 {
            difference |= candidate[index]
                ^ concealed(&encoded[index])
                ^ concealed(&MASK_A[index])
                ^ concealed(&MASK_B[index]);
        }
        matched |= u8::from(difference == 0);
    }
    matched != 0
}
