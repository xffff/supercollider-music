/************************************	*/
/* Ensemble Piece			       	*/
/*	MIXER						*/
/************************************	*/

(
	// every channel requires a synth
	// these are located in the ~output group
	// and output to the hardware outputs 
	// dependant on ~numchans
	// these must be configured manually
	// as the EQ and db parameters are individual
	
	// db and pan arrays;
	~mixchan_db = -100!~numchans;
	~mixchan_pan = 0!~numchans;
	
	~mixchan_db[0] = 0; ~mixchan_pan[0] = 0;
	~mixchan_db[1] = 0; ~mixchan_pan[1] = 0;
	~mixchan_db[2] = 0; ~mixchan_pan[2] = 0;
	~mixchan_db[3] = 0; ~mixchan_pan[3] = 0;
	~mixchan_db[4] = 0; ~mixchan_pan[4] = 0;
	~mixchan_db[5] = 0; ~mixchan_pan[5] = 0;
	~mixchan_db[6] = 0; ~mixchan_pan[6] = 0;
	~mixchan_db[7] = 0; ~mixchan_pan[7] = 0;
	~mixchan_db[8] = 0; ~mixchan_pan[8] = 0;
	~mixchan_db[9] = 0; ~mixchan_pan[9] = 0;
	~mixchan_db[10] = 0; ~mixchan_pan[10] = 0;
	~mixchan_db[11] = 0; ~mixchan_pan[11] = 0;
	~mixchan_db[12] = 0; ~mixchan_pan[12] = 0;
	~mixchan_db[13] = 0; ~mixchan_pan[13] = 0;
	~mixchan_db[14] = 0; ~mixchan_pan[14] = 0;
	~mixchan_db[15] = 0; ~mixchan_pan[15] = 0;
	~mixchan_db[16] = 0; ~mixchan_pan[16] = 0;
	~mixchan_db[17] = 0; ~mixchan_pan[17] = 0;
	~mixchan_db[18] = 0; ~mixchan_pan[18] = 0;
	~mixchan_db[19] = 0; ~mixchan_pan[19] = 0;
	~mixchan_db[20] = 0; ~mixchan_pan[20] = 0;
	~mixchan_db[21] = 0; ~mixchan_pan[21] = 0;
	~mixchan_db[22] = 0; ~mixchan_pan[22] = 0;
	~mixchan_db[23] = 0; ~mixchan_pan[23] = 0;
	~mixchan_db[24] = 0; ~mixchan_pan[24] = 0;
	~mixchan_db[25] = 0; ~mixchan_pan[25] = 0;
	~mixchan_db[26] = 0; ~mixchan_pan[26] = 0;
	
	
	// dry instruments come directly in from MaxMSP 
	// so we don't require extra subBusses for them
	
	// flute
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(0,1), // ~master_mix_bus.subBus(0,1),
			\db, ~mixchan_db[0],
			\pan, ~mixchan_pan[0]
		], 
		target: ~output, 
		addAction: \addToHead
	);
	
	// bass flute
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(1,1), // ~master_mix_bus.subBus(1,1),
			\db, ~mixchan_db[1],
			\pan, ~mixchan_pan[1]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// bass clarinet
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(2,1), // ~master_mix_bus.subBus(2,1),
			\db, ~mixchan_db[2],
			\pan, ~mixchan_pan[2]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// bassoon 1 & 2
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(3,1), // ~master_mix_bus.subBus(3,1),
			\db, ~mixchan_db[3],
			\pan, ~mixchan_pan[3]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// trumpet 1 & 2
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(4,1), // ~master_mix_bus.subBus(4,1),
			\db, ~mixchan_db[4],
			\pan, ~mixchan_pan[4]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// trombone 1 & 2
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(5,1), // ~master_mix_bus.subBus(5,1),
			\db, ~mixchan_db[5],
			\pan, ~mixchan_pan[5]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// sax 1 & 2
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(6,1), // ~master_mix_bus.subBus(6,1),
			\db, ~mixchan_db[6],
			\pan, ~mixchan_pan[6]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// violin 1 & 2
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(7,1), // ~master_mix_bus.subBus(7,1),
			\db, ~mixchan_db[7],
			\pan, ~mixchan_pan[7]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// violin 3 & 4
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(8,1), // ~master_mix_bus.subBus(8,1),
			\db, ~mixchan_db[8],
			\pan, ~mixchan_pan[8]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// viola 1 & 2
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(9,1), // ~master_mix_bus.subBus(9,1),
			\db, ~mixchan_db[9],
			\pan, ~mixchan_pan[9]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// violoncello
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(10,1), // ~master_mix_bus.subBus(10,1),
			\db, ~mixchan_db[10],
			\pan, ~mixchan_pan[10]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// double bass
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(11,1), // ~master_mix_bus.subBus(11,1),
			\db, ~mixchan_db[11],
			\pan, ~mixchan_pan[11]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// crotales
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(12,1), // ~master_mix_bus.subBus(12,1),
			\db, ~mixchan_db[12],
			\pan, ~mixchan_pan[12]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// tam tam
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(13,1), // ~master_mix_bus.subBus(13,1),
			\db, ~mixchan_db[13],
			\pan, ~mixchan_pan[13]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// bass drum
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(14,1), // ~master_mix_bus.subBus(14,1),
			\db, ~mixchan_db[14],
			\pan, ~mixchan_pan[14]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// synthesiser 
	Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(15,1), // ~master_mix_bus.subBus(15,1),
			\db, ~mixchan_db[15],
			\pan, ~mixchan_pan[15]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// convolution
	Synth(\mixerchannel, [
			\in, ~master_mix_bus.subBus(16,1),
			\db, ~mixchan_db[16],
			\pan, ~mixchan_pan[16]
		], 
		target: ~output, 
		addAction: \addToHead
	);
	
	// warp
	Synth(\mixerchannel, [
			\in, ~master_mix_bus.subBus(17,1),
			\db, ~mixchan_db[17],
			\pan, ~mixchan_pan[17]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// freeze
	Synth(\mixerchannel, [
			\in, ~master_mix_bus.subBus(18,1),
			\db, ~mixchan_db[18],
			\pan, ~mixchan_pan[18]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// frequency shifter
	Synth(\mixerchannel, [
			\in, ~master_mix_bus.subBus(19,1),
			\db, ~mixchan_db[19],
			\pan, ~mixchan_pan[19]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// pitch shifter
	Synth(\mixerchannel, [
			\in, ~master_mix_bus.subBus(20,1),
			\db, ~mixchan_db[20],
			\pan, ~mixchan_pan[20]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// grain
	Synth(\mixerchannel, [
			\in, ~master_mix_bus.subBus(21,1),
			\db, ~mixchan_db[21],
			\pan, ~mixchan_pan[21]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// play buffer
	Synth(\mixerchannel, [
			\in, ~master_mix_bus.subBus(22,1),
			\db, ~mixchan_db[22],
			\pan, ~mixchan_pan[22]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// feedback delay
	Synth(\mixerchannel, [
			\in, ~master_mix_bus.subBus(23,1),
			\db, ~mixchan_db[23],
			\pan, ~mixchan_pan[23]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// reverb
	Synth(\mixerchannel, [
			\in, ~master_mix_bus.subBus(24,1),
			\db, ~mixchan_db[24],
			\pan, ~mixchan_pan[24]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// bitcrush / distortion
	Synth(\mixerchannel, [
			\in, ~master_mix_bus.subBus(25,1),
			\db, ~mixchan_db[25],
			\pan, ~mixchan_pan[25]
		], 
		target: ~output, 
		addAction: \addToHead
	);

	// halaphone
	Synth(\mixerchannel, [
			\in, ~master_mix_bus.subBus(26,6), // hala has a max of 6 outputs at the moment
			\db, ~mixchan_db[26],
			\pan, ~mixchan_pan[26]
		], 
		target: ~output, 
		addAction: \addToHead
	);
)