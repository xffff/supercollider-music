/************************************	*/
/* Untitled Piece	     		       	*/
/*	MIXER			        			*/
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

~numchans.do{|i|
	~mixchan_db[i] = 0;
	~mixchan_pan[i] = 0;
};
if(~dawversion == false, {
	~numdrychans.do{|i|
		Synth(\mixerchannel, [
			\in, ~master_dry_bus.subBus(i,1),
			\db, ~mixchan_db[i],
			\pan, ~mixchan_pan[i],
			\out, 0
			],
			target: ~output,
			addAction: \addToHead
		);
	};
	(~numfxchans-~numoutchans).do{|i|
		i=i+~numdrychans;
		Synth(\mixerchannel, [
			\in, ~master_mix_bus.subBus(i,1),
			\db, ~mixchan_db[i],
			\pan, ~mixchan_pan[i],
			\out, 0
			],
			target: ~output,
			addAction: \addToHead
		);
	};

	// halaphone takes up the remaining channels for spat
	Synth(\mixerchannel, [
		\in, ~master_mix_bus.subBus(~numfxchans-~numoutchans,~numoutchans),
		\db, ~mixchan_db[~numfxchans-~numoutchans],
		\pan, ~mixchan_pan[~numfxchans-~numoutchans],
		\out, 0
		],
		target: ~output,
		addAction: \addToHead
	);
	},{
		~numdrychans.do{|i|
			Synth(\dawmixerchannel, [
				\in, ~master_dry_bus.subBus(i,1),
				\db, ~mixchan_db[i],
				\pan, ~mixchan_pan[i],
				\out, i
				],
				target: ~output,
				addAction: \addToHead
			);
		};
		(~numfxchans-~numoutchans).do{|i|
			i=i+~numdrychans;
			Synth(\dawmixerchannel, [
				\in, ~master_mix_bus.subBus(i,1),
				\db, ~mixchan_db[i],
				\pan, ~mixchan_pan[i],
				\out, i
				],
				target: ~output,
				addAction: \addToHead
			);
		};

		// halaphone takes up the remaining channels for spat
		Synth(\dawmixerchannel, [
			\in, ~master_mix_bus.subBus(~numfxchans-~numoutchans,~numoutchans),
			\db, ~mixchan_db[~numfxchans-~numoutchans],
			\pan, ~mixchan_pan[~numfxchans-~numoutchans],
			\out, ~numfxchans-~numoutchans
			],
			target: ~output,
			addAction: \addToHead
		);

	}
);
)