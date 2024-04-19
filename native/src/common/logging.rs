#[cfg(debug_assertions)]
use android_logger::{Config, FilterBuilder};
#[cfg(debug_assertions)]
use log::LevelFilter;

#[cfg(debug_assertions)]
use crate::EXTENDED_LOGGING;

#[cfg(debug_assertions)]
pub fn init_logger() {
    let config = Config::default()
        .with_max_level(LevelFilter::Trace) // limit log level
        .with_tag("rasp-android")
        .with_filter(
            // configure messages for specific crate
            if EXTENDED_LOGGING {
                FilterBuilder::new().build()
            } else {
                FilterBuilder::new()
                    .parse("debug,hello::crate=error")
                    .build()
            },
        );

    android_logger::init_once(config);
    log_android(LevelFilter::Debug, "Native logging enabled");
}

#[cfg(debug_assertions)]
pub fn log_android(level: LevelFilter, message: &str) {
    match level {
        LevelFilter::Off => {}
        LevelFilter::Error => error!("{}", message),
        LevelFilter::Warn => warn!("{}", message),
        LevelFilter::Info => info!("{}", message),
        LevelFilter::Debug => debug!("{}", message),
        LevelFilter::Trace => trace!("{}", message),
    }
}

#[cfg(not(debug_assertions))]
pub fn init_logger() {
    // do nothing
}
