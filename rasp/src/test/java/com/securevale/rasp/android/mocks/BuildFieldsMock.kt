package com.securevale.rasp.android.mocks

import com.securevale.rasp.android.os.BuildFields
import io.mockk.every
import io.mockk.mockkObject

internal fun mockDeviceData(
    device: String = "dream2lte",
    bootloader: String = "G955FXXUCDUD1",
    model: String = "SM-G955F",
    board: String = "universal8895",
    brand: String = "samsung",
    manufacturer: String = "samsung",
    hardware: String = "G955FXXUCDUD1",
    fingerprint: String = "samsung/dream2ltexx/dream2lte:9/PPR1.180610.011/G955FXXUCDUD1:user/release-keys",
    product: String = "dream2ltexx",
    tags: String = "release-keys",
    host: String = ""
) {
    mockkObject(BuildFields)
    every { BuildFields.bootLoader } returns bootloader
    every { BuildFields.device } returns device
    every { BuildFields.model } returns model
    every { BuildFields.board } returns board
    every { BuildFields.brand } returns brand
    every { BuildFields.manufacturer } returns manufacturer
    every { BuildFields.hardware } returns hardware
    every { BuildFields.fingerprint } returns fingerprint
    every { BuildFields.product } returns product
    every { BuildFields.tags } returns tags
    every { BuildFields.host } returns host
}
