let Deno = {}

Deno.build = {
    target: MiscInfo.getTarget(),
    arch: MiscInfo.getArch(),
    os: MiscInfo.getOS(),
    vendor: MiscInfo.getVendor(),
    env: MiscInfo.getEnv()
}